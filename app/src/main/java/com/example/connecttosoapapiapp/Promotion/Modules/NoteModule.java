package com.example.connecttosoapapiapp.Promotion.Modules;

public class NoteModule {
    String note_id;
    String note_name;

    public NoteModule(String note_id, String note_name) {
        this.note_id = note_id;
        this.note_name = note_name;
    }

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public String getNote_name() {
        return note_name;
    }

    public void setNote_name(String note_name) {
        this.note_name = note_name;
    }
}
