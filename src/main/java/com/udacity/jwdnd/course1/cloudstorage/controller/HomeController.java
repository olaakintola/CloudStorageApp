package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileStorageService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

@Controller
public class HomeController {

    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private FileStorageService fileStorageService;

    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService, FileStorageService fileStorageService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/home")
    public String getHomePage(@ModelAttribute("noteObject") Note note, @ModelAttribute("credentialObject") Credential credential, Model model, Authentication authentication){
        String username = authentication.getName();
        User user = this.userService.getUser(username);
        Integer userid = user.getUserid();

        model.addAttribute("userNotes", this.noteService.displayAllNotes(userid));
        model.addAttribute("unencryptedPasswordMap", this.credentialService.getUnecryptedPassword(userid));
        model.addAttribute("userCredentials", this.credentialService.displayAllCredentials(userid));
        model.addAttribute("files", this.fileStorageService.loadAllFiles(userid));
        return "home";
    }


    @PostMapping("home/note")
    public String addNote(@ModelAttribute("noteObject") Note note, @ModelAttribute("credentialObject") Credential credential, Model model, Authentication authentication){
        String username = authentication.getName();
        User user = this.userService.getUser(username);
        Integer userid = user.getUserid();
        note.setUserId(userid);

        String taskError = null;

        if(note.getNoteid() == null){
            try {
                this.noteService.addNote(note, username);
            }catch (Exception e){
                taskError = "Note cannot be created";
            }

        }else{
            try {
                this.noteService.updateNote(note, username);
            }catch (Exception e){
                taskError = "Note cannot be updated";
            }
        }

        if(taskError == null){
            model.addAttribute("successMessage", true);
            model.addAttribute("failureMessage", false);
        }else{
            model.addAttribute("failureMessage", taskError);
        }

        return "result";
    }

    @GetMapping("/home/deleteNote/{id}")
    public String deleteNote(@PathVariable("id") Integer id, @ModelAttribute("noteObject") Note note, Model model, Authentication authentication) {

        String taskError = null;
        try {
            this.noteService.deleteNote(id);
        }
        catch (Exception e) {
            taskError = "Error: Selected Note cannot be deleted";
        }

        if(taskError == null){
            model.addAttribute("successMessage", true);
            model.addAttribute("failureMessage", false);
        }else{
            model.addAttribute("failureMessage", taskError);
        }
        return "result";
    }

    @PostMapping("/home/credential")
    public String addCredential(@ModelAttribute("credentialObject") Credential credential, @ModelAttribute("noteObject") Note note, Model model, Authentication authentication){

        String taskError = null;
        /* Try creating a valid URL */
        try {
            new URL(credential.getUrl() ).toURI();
        }
        // If there was an Exception while creating URL object
        catch (Exception e) {
            taskError = "This is an invalid url";
        }

        if(taskError == null) {
            String username = authentication.getName();
            if(credential.getCredentialid() == null){
                this.credentialService.addCredential(credential, username);
            }else{
                this.credentialService.updateCredential(credential, username);
            }
            model.addAttribute("successMessage", true);
            model.addAttribute("failureMessage", false);
        }else{
            model.addAttribute("failureMessage", taskError);
        }

        return "result";
    }


    @GetMapping("/home/deleteCredential/{id}")
    public String deleteCredential(@PathVariable("id") Integer id, @ModelAttribute("credentialObject") Credential credential ,@ModelAttribute("noteObject") Note note, Model model, Authentication authentication) {

        String taskError = null;
        try {
            this.credentialService.deleteCredential(id);
        }
        catch (Exception e) {
            taskError = "Error: Selected Url Credential cannot be deleted";
        }

        if(taskError == null){
            model.addAttribute("successMessage", true);
            model.addAttribute("failureMessage", false);
        }else{
            model.addAttribute("failureMessage", taskError);
        }

        return "result";
    }


    @PostMapping("/home/file-upload")
    public String handleFileUpload(@RequestParam("fileUpload")MultipartFile fileUpload, Model model, Authentication authentication) throws IOException {

        String username = authentication.getName();
        User user = this.userService.getUser(username);
        Integer userid = user.getUserid();

        String taskError = null;

        if(fileUpload.isEmpty() ){
            taskError = "Failed to store empty file.";
        }

        if(taskError == null){
            String fileName = fileUpload.getOriginalFilename();
            boolean fileNameInUseByUser = this.fileStorageService.isFileNameInUseByUser(userid, fileName);
            if(fileNameInUseByUser){
                taskError = "You have already stored a file under similar name";
            }
        }

        if(taskError == null){
            this.fileStorageService.storeFile(fileUpload, username);
            model.addAttribute("successMessage", true);
            model.addAttribute("failureMessage", false);
        }else{
            model.addAttribute("failureMessage", taskError);
        }

        return "result";
    }

    @GetMapping("/home/download-file/{id}")
    public void downloadFile(@PathVariable("id") Integer id, HttpServletResponse httpServletResponse) throws IOException {
        UserFile userFile = this.fileStorageService.loadSingleFile(id);
        httpServletResponse.setContentType(userFile.getContenttype());
        httpServletResponse.setContentLength(Integer.valueOf( userFile.getFilesize() ) );
        httpServletResponse.setHeader("Content-Disposition","attachment; filename=\"" + userFile.getFilename() +"\"");

        FileCopyUtils.copy(userFile.getFiledata(), httpServletResponse.getOutputStream() );
    }

    @GetMapping("/home/deleteFile/{id}")
    public String deleteFile(@PathVariable("id") Integer id, @ModelAttribute("credentialObject") Credential credential ,@ModelAttribute("noteObject") Note note, Model model, Authentication authentication) {

        String taskError = null;
        try {
            this.fileStorageService.deleteFile(id);
        }
        catch (Exception e) {
            taskError = "Error: Selected File cannot be deleted";
        }

        if(taskError == null){
            model.addAttribute("successMessage", true);
            model.addAttribute("failureMessage", false);
        }else{
            model.addAttribute("failureMessage", taskError);
        }

        return "result";
    }

}
