package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileStorageService {

    private final FileMapper fileMapper;
    private final UserService userService;

    public FileStorageService(FileMapper fileMapper, UserService userService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
    }

    public int storeFile(MultipartFile fileUpload, String username) throws IOException {
        User user = this.userService.getUser(username);
        Integer userid = user.getUserid();
        return fileMapper.insertUserFile(new UserFile(null, fileUpload.getOriginalFilename(),fileUpload.getContentType(), Long.toString(fileUpload.getSize() ) , userid.intValue(), fileUpload.getInputStream() ));
    }

    public List<UserFile> loadAllFiles(Integer userid){

        return  fileMapper.retrieveAllUserFiles(userid);
    }

    public UserFile loadSingleFile(Integer fileId){
        return fileMapper.getUserFile(fileId);
    }

    public void deleteFile(Integer id){
        this.fileMapper.deleteUserFile(id);
    }

    public boolean isFileNameInUseByUser(Integer userid, String fileName){

        List<UserFile> userFiles = fileMapper.retrieveAllUserFiles(userid);
        for(UserFile file: userFiles){
            if(file.getFilename().equals(fileName)){
                return true;
            }
        }
        return false;
    }

}
