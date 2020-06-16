package ro.go.redhomeserver.tom.enums;

public enum RequestType {
    Med("Medical"),
    Rel("Rest"),
    Fam("Family situation"),
    Hof("Home Office");


    private final String displayValue;

    RequestType(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
