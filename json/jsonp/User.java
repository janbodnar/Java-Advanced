package com.zetcode;

import java.time.LocalDate;

class User {

    private String name;
    private String occupation;
    private LocalDate born;

    public User(String name, String occupation, LocalDate born) {
        this.name = name;
        this.occupation = occupation;
        this.born = born;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public LocalDate getBorn() {
        return born;
    }

    public void setBorn(LocalDate born) {
        this.born = born;
    }

    @Override
    public String toString() {

        final var sb = new StringBuilder("User{");
        sb.append("name='").append(name).append('\'');
        sb.append(", occupation='").append(occupation).append('\'');
        sb.append(", born=").append(born);
        sb.append('}');
        return sb.toString();
    }
}
