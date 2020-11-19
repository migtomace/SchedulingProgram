package Model;

/**
 *
 * Contact Model
 *
 * */
public class Contact {

    private String name, email;
    private int id;

    /** Empty constructor*/
    public Contact(){

    }

    /** Returns contact ID
     * @return id*/
    public int getId() {
        return id;
    }

    /** Sets contact ID
     * @param id - int id*/
    public void setId(int id) {
        this.id = id;
    }

    /** Returns contact name
     * @return name*/
    public String getName() {
        return name;
    }

    /** Sets contact name
     * @param name - String name*/
    public void setName(String name) {
        this.name = name;
    }

    /** Returns contact email
     * @return email*/
    public String getEmail() {
        return email;
    }

    /** Sets contact email
     * @param email - String email*/
    public void setEmail(String email) {
        this.email = email;
    }
}
