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

        if(note.getNoteid() == null){
            this.noteService.addNote(note, username);
        }else{
            this.noteService.updateNote(note, username);
        }

        return "redirect:/home";
    }

    @GetMapping("/home/deleteNote/{id}")
    public String deleteNote(@PathVariable("id") Integer id, @ModelAttribute("noteObject") Note note, Model model, Authentication authentication) {

        this.noteService.deleteNote(id);
        return "redirect:/home";
    }

    @PostMapping("/home/credential")
    public String addCredential(@ModelAttribute("credentialObject") Credential credential, @ModelAttribute("noteObject") Note note, Model model, Authentication authentication){

        String username = authentication.getName();
        if(credential.getCredentialid() == null){
            this.credentialService.addCredential(credential, username);
        }else{
            this.credentialService.updateCredential(credential, username);
        }

        return "redirect:/home";
    }


    @GetMapping("/home/deleteCredential/{id}")
    public String deleteCredential(@PathVariable("id") Integer id, @ModelAttribute("credentialObject") Credential credential ,@ModelAttribute("noteObject") Note note, Model model, Authentication authentication) {

        this.credentialService.deleteCredential(id);
        return "redirect:/home";
    }


    @PostMapping("/home/file-upload")
    public String handleFileUpload(@RequestParam("fileUpload")MultipartFile fileUpload, Model model, Authentication authentication) throws IOException {

        String username = authentication.getName();
        User user = this.userService.getUser(username);
        Integer userid = user.getUserid();

        String uploadFileError = null;

        if(fileUpload.isEmpty() ){
            uploadFileError = "Failed to store empty file.";
        }

        if(uploadFileError == null){
            String fileName = fileUpload.getOriginalFilename();
            boolean fileNameInUseByUser = this.fileStorageService.isFileNameInUseByUser(userid, fileName);
            if(fileNameInUseByUser){
                uploadFileError = "You have already stored a file under similar name";
            }
        }

        if(uploadFileError == null){
            this.fileStorageService.storeFile(fileUpload, username);
            model.addAttribute("uploadFileSuccess", true);
            model.addAttribute("uploadFileError", false);
        }else{
            model.addAttribute("uploadFileError", uploadFileError);
        }

        return "result";
    }

    @GetMapping("/displayAllFiles")
    public String getAllUploadedFiles(@ModelAttribute("credentialObject") Credential credential, @ModelAttribute("noteObject") Note note, Model model, Authentication authentication){

        return "redirect:/home";
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

        this.fileStorageService.deleteFile(id);
        return "redirect:/home";
    }

}
