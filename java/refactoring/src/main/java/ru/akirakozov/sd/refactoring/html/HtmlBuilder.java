package ru.akirakozov.sd.refactoring.html;

public class HtmlBuilder {
    private final StringBuilder impl = new StringBuilder("<html><body>\n");

    public void line(String text) {
        impl.append(text).append("\n");
    }

    public void h1Header(String text) {
        line("<h1>" + text + "</h1>");
    }

    public void brLine(String text) {
        line(text + "</br>");
    }

    @Override
    public String toString() {
        return impl.toString() + "</body></html>";
    }
}
