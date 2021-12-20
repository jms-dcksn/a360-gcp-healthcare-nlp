package com.automationanywhere.botcommand.samples.Utils;

public class GoogleHC {


    public String getToken() {
        return Token;
    }

    public String getLocation() {
        return Location;
    }

    public String getProjectId() {
        return ProjectId;
    }

    String Token;
    String Location;
    String ProjectId;

    public GoogleHC(String Location, String Token, String ProjectId){
        this.Location = Location;
        this.Token = Token;
        this.ProjectId = ProjectId;
    }
}