package kz.logitex.kartoteka.model;

public enum Status {
    OPENED("Открыта"),
    CLOSED("Закрыта");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
