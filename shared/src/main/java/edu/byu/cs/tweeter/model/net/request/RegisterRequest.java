package edu.byu.cs.tweeter.model.net.request;

public class RegisterRequest extends LogRegRequest {
    private String firstName;
    private String lastName;
    private String image;

    private RegisterRequest() { super(null, null); }

    public RegisterRequest(String firstName, String lastName, String username, String password, String image) {
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
