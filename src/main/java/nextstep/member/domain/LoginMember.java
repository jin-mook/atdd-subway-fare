package nextstep.member.domain;

import nextstep.auth.domain.UserDetails;

import java.util.Objects;

public class LoginMember implements UserDetails {
    private final Long id;
    private final String email;
    private final String password;
    private final int age;

    public LoginMember(Long id, String email, String password, int age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginMember that = (LoginMember) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
