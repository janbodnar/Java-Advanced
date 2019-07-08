package com.zetcode;

public class User {

    private String name;
    private boolean single;

    public User(String name, boolean single) {

        this.name = name;
        this.single = single;
    }

    public boolean isSingle() {

        return single;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("name='").append(name).append('\'');
        sb.append(", single=").append(single);
        sb.append('}');
        return sb.toString();
    }
}
