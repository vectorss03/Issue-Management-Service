import com.se14.view.SwingView;

public class ApplicationExample {
    public static void main(String[] args) {
        System.out.println(new ApplicationExample().helloWorld());
        SwingView view = new SwingView();
    }

    public String helloWorld() {
        return "Hello World!";
    }
}