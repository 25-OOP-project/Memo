import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;

public class MemoPanel extends JPanel {

    private JComboBox<String> lectureComboBox;
    private JTextField dateField;
    private JTextArea memoTextArea;
    private JButton saveButton, loadButton, deleteButton;
    private static final String MEMO_ROOT = "./memo/";

    public MemoPanel(String[] lectureNames) {
        setLayout(new BorderLayout());

        // 상단: 과목 선택 + 날짜 입력
        JPanel topPanel = new JPanel(new FlowLayout());

        topPanel.add(new JLabel("강의 선택:"));
        lectureComboBox = new JComboBox<>(lectureNames);
        topPanel.add(lectureComboBox);

        topPanel.add(new JLabel("날짜 입력 (yyyy-MM-dd):"));
        dateField = new JTextField(10);
        dateField.setText(LocalDate.now().toString());  // 기본값: 오늘
        topPanel.add(dateField);

        add(topPanel, BorderLayout.NORTH);

        // 중앙: 메모 입력 영역
        memoTextArea = new JTextArea(15, 40);
        JScrollPane scrollPane = new JScrollPane(memoTextArea);
        add(scrollPane, BorderLayout.CENTER);

        // 하단: 버튼 3종
        JPanel buttonPanel = new JPanel();
        saveButton = new JButton("저장");
        loadButton = new JButton("불러오기");
        deleteButton = new JButton("삭제");

        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 이벤트 연결
        saveButton.addActionListener(e -> saveMemo());
        loadButton.addActionListener(e -> loadMemo());
        deleteButton.addActionListener(e -> deleteMemo());

        // memo 디렉토리 생성
        File rootDir = new File(MEMO_ROOT);
        if (!rootDir.exists()) rootDir.mkdirs();
    }

    private void saveMemo() {
        String lectureName = (String) lectureComboBox.getSelectedItem();
        String dateStr = dateField.getText().trim();

        String lectureDirPath = MEMO_ROOT + lectureName + "/";
        File lectureDir = new File(lectureDirPath);
        if (!lectureDir.exists()) lectureDir.mkdirs();

        String fileName = lectureDirPath + dateStr + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(memoTextArea.getText());
            JOptionPane.showMessageDialog(this, "메모 저장 완료!\n" + fileName);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "저장 실패: " + ex.getMessage());
        }
    }

    private void loadMemo() {
        String lectureName = (String) lectureComboBox.getSelectedItem();
        String dateStr = dateField.getText().trim();

        String lectureDirPath = MEMO_ROOT + lectureName + "/";
        String fileName = lectureDirPath + dateStr + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            memoTextArea.setText("");
            String line;
            while ((line = reader.readLine()) != null) {
                memoTextArea.append(line + "\n");
            }
            JOptionPane.showMessageDialog(this, "메모 불러오기 완료!\n" + fileName);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "불러오기 실패: " + ex.getMessage());
        }
    }

    private void deleteMemo() {
        String lectureName = (String) lectureComboBox.getSelectedItem();
        String dateStr = dateField.getText().trim();

        String lectureDirPath = MEMO_ROOT + lectureName + "/";
        String fileName = lectureDirPath + dateStr + ".txt";
        File memoFile = new File(fileName);

        if (memoFile.exists()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "정말 삭제하시겠습니까?\n" + fileName,
                    "삭제 확인", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (memoFile.delete()) {
                    memoTextArea.setText("");
                    JOptionPane.showMessageDialog(this, "삭제 완료!\n" + fileName);
                } else {
                    JOptionPane.showMessageDialog(this, "삭제 실패 😢");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "삭제할 파일이 없습니다!");
        }
    }
}

