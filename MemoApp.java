import javax.swing.*;

public class MemoApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("강의 메모 패널");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new MemoPanel(new String[]{
                "객체지향프로그래밍",
                "오픈소스프로그래밍",
                "데이터구조"
            }));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
