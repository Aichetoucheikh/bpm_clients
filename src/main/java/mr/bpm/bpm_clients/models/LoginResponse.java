package mr.bpm.bpm_clients.models;

import java.util.List;

public class LoginResponse {
    private String token;
    private String roles;
    private String nom;
    private String photoUrl;
    private List<String> permissions;

    public LoginResponse(String token, String roles, String nom, String photoUrl, List<String> permissions) {
        this.token = token;
        this.roles = roles;
        this.nom = nom;
        this.photoUrl = photoUrl;
        this.permissions = permissions;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}