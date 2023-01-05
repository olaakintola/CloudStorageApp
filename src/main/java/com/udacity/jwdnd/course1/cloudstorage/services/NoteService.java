package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;
    private UserService userService;

    public NoteService(NoteMapper noteMapper, UserService userService) {
        this.noteMapper = noteMapper;
        this.userService = userService;
    }

    public int addNote(Note note, String username){
        System.out.println(username);
        String notetitle = note.getNotetitle();
        String notedescription = note.getNotedescription();
        User user = this.userService.getUser(username);
        Integer userid = user.getUserid();
        System.out.println(userid.intValue() + "userid");
        System.out.println(userid);
        return noteMapper.insertNote(new Note(null, notetitle, notedescription, userid.intValue() ));
    }

    public void updateNote(Note note, String username){
        System.out.println(note.getNoteid().intValue() + "note id");
        Integer noteid = note.getNoteid();
        String notetitle = note.getNotetitle();
        String notedescription = note.getNotedescription();
        User user = this.userService.getUser(username);
        Integer userid = user.getUserid();

        this.noteMapper.updateNote( noteid.intValue(), notetitle, notedescription, userid.intValue()  );
        System.out.println(note.getNoteid().intValue() + "note id");

    }

    public void deleteNote(Integer noteid){
        this.noteMapper.deleteNote(noteid);
    }

    public List<Note> displayAllNotes(Integer userid){
        return noteMapper.retrieveAllNotes(userid);
    }
}
