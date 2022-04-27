package com.BrainFlux.AutoInput.service;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Service
public class AutoStreaming {

    private String IP;
    private int PORT;
    private String USER;
    private String PASSWORD;

    //    outputSetting
    private String PANEL;
    private String MMXFILE;

    //    Directory
    private String EEGDIRECTORY;
    private String PSCLIDIRECTORY;
    private String CSVDIRECTORY;
    private String DESTINATION;
    private String FINISHEDFILES;
    private String LOGPATH;



    public AutoStreaming(String IP, int PORT, String USER, String PASSWORD, String PANEL, String MMXFILE, String EEGDIRECTORY,
                         String CSVDIRECTORY, String PSCLIDIRECTORY, String DESTINATION, String FINISHEDFILES, String LOGPATH){
        this.IP = IP;
        this.PORT = PORT;
        this.USER = USER;
        this.PASSWORD = PASSWORD;
        this.PANEL = PANEL;
        this.MMXFILE = MMXFILE;
        this.EEGDIRECTORY = EEGDIRECTORY;
        this.PSCLIDIRECTORY = PSCLIDIRECTORY;
        this.CSVDIRECTORY = CSVDIRECTORY;
        this.DESTINATION = DESTINATION;
        this.FINISHEDFILES = FINISHEDFILES;
        this.LOGPATH = LOGPATH;
    }

    public AutoStreaming(){
    }


    private void cronJob(int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                // process erd files to generate lay files
                List<File> ergFileList = findERGFiles(EEGDIRECTORY);
                if(ergFileList.size() != 0){
                    setLog(LocalDateTime.now()+ ": start processing ERD files");
                    processErdFiles(ergFileList);
                }

                // convert lay files to csv files
                List<File> layFileList = findProcessedFiles(EEGDIRECTORY);
                if(layFileList.size() != 0){
                    setLog(LocalDateTime.now()+ ": start converting lay files");
                    convertToCSV(layFileList);
                }

                // send csv files
//                List<File> toBesent = getAllCSV(CSVDIRECTORY);
//                if(toBesent.size() != 0){
//                    setLog(LocalDateTime.now()+ ": start sending csv files");
//                    convertToCSV(toBesent);
//                }


            }
        }, cal.getTime(), 24 * 60 * 60 * 1000);
    }

    private Boolean scpSend(String path){
        Connection connection = new Connection(IP,PORT);
        try {
            connection.connect();
            boolean isAuthenticated = connection.authenticateWithPassword(USER,PASSWORD);
            if(!isAuthenticated){
                setLog(LocalDateTime.now()+": fail to connect to mac");
                return false;
            }
            SCPClient scpClient = connection.createSCPClient();
            scpClient.put(path,DESTINATION);
            connection.close();
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private List<File> findERGFiles (String dir){
        File directory = new File(dir);
        File[] files = directory.listFiles();
        LinkedList<File> queueList = new LinkedList<>();
        List<File> eegFileList = new ArrayList<>();
        if(files == null || files.length==0){
            return eegFileList;
        }
        for (int i = 0; i <files.length;i++){
            if(files[i].isDirectory()){
                queueList.add(files[i]);
            }
        }

        while (! queueList.isEmpty()){
            File tempDirectory = queueList.removeFirst();
            File[] currectFiles = tempDirectory.listFiles();
            for (int j = 0; j< currectFiles.length;j ++){
                if(currectFiles[j].isDirectory()){
                    queueList.add(currectFiles[j]);
                }else{
                    if(currectFiles[j].getName().endsWith(".eeg")){
                        String erdFile = currectFiles[j].getAbsolutePath().replace(".eeg",".erd");
                        eegFileList.add(new File(erdFile));
                    }
                }
            }
        }

        return eegFileList;
    };


    private List<File> findProcessedFiles(String dir){
        File directory = new File(dir);
        File[] files = directory.listFiles();
        System.out.println(files[0]);
        LinkedList<File> queueList = new LinkedList<>();
        List<File> layFileList = new ArrayList<>();
        if(files == null || files.length==0){
            return layFileList;
        }
        for (int i = 0; i <files.length;i++){
            if(files[i].isDirectory()){
                queueList.add(files[i]);
            }
        }

        while (! queueList.isEmpty()){
            File tempDirectory = queueList.removeFirst();
            File[] currectFiles = tempDirectory.listFiles();
            for (int j = 0; j< currectFiles.length;j ++){
                if(currectFiles[j].isDirectory()){
                    queueList.add(currectFiles[j]);
                }else{
                    if(currectFiles[j].getName().endsWith(".lay")){
                        layFileList.add(currectFiles[j]);
                    }
                }
            }
        }

        return layFileList;
    }



    private List<File> processErdFiles(List<File> fileList){
        List<File> successfulFiles = new ArrayList<>();
        for (File file: fileList){
            String command = String.format("%sPSCLI /panel=\"%s\" /FileType=XLTEK  /SourceFile=\"%s\" /Process /MMX=\"%s\" ",PSCLIDIRECTORY,PANEL,file.getAbsolutePath(),MMXFILE);
            System.out.println("erd process command: " + command);
            try{
                Process process = Runtime.getRuntime().exec(command);
                new RunThread(process.getInputStream(), "INFO").start();
                new RunThread(process.getErrorStream(),"ERR").start();
                if( process.waitFor() == 0){
                    setLog(LocalDateTime.now()+": process file successfully: "+ file.getName());
                    successfulFiles.add(file);
                }else {
                    setLog(LocalDateTime.now()+": process file failed: "+ file.getName());
                }
            }catch (Exception e){
                setLog(LocalDateTime.now()+": process file error occurred: "+ file.getName());
            }
        }
        return successfulFiles;
    }



    private List<File> convertToCSV( List<File> layFileToConvert){
        List<File> successfulCSV = new ArrayList<>();

        for (File layFile : layFileToConvert){
            String arFileOutput = CSVDIRECTORY + layFile.getParent().toUpperCase() + "ar.csv";
            String noarFileOutput = CSVDIRECTORY + layFile.getParent().toUpperCase() + "noar.csv";
            String arCommand = String.format("%sPSCLI /panel=\"%s\" /SourceFile=\"%s\" /FileType=XLTEK /Process /OutputFile=\"%s\" /MMX=\"%s\" /ExportCSV",PSCLIDIRECTORY,PANEL,layFile,arFileOutput,MMXFILE);
            String noarCommand = String.format("%sPSCLI /panel=\"%s\" /SourceFile=\"%s\" /FileType=XLTEK /Process /OutputFile=\"%s\" /MMX=\"%s\" /ExportCSV",PSCLIDIRECTORY,PANEL,layFile,noarFileOutput,MMXFILE);

            System.out.println("csv convert command: " + arCommand);

            try{
                int value = switchRegistry("ar");
                Process process = Runtime.getRuntime().exec(arCommand);
                new RunThread(process.getInputStream(), "INFO").start();
                new RunThread(process.getErrorStream(),"ERR").start();
                value += process.waitFor();
                if(value == 0){
                    successfulCSV.add(new File(arFileOutput));
                    System.out.println(LocalDateTime.now()+": generate ar csv file successful: "+ arFileOutput);
                    value += switchRegistry("noar");
                    process = Runtime.getRuntime().exec(noarCommand);
                    new RunThread(process.getInputStream(), "INFO").start();
                    new RunThread(process.getErrorStream(),"ERR").start();
                    value += process.waitFor();
                    if(value == 0){
                        successfulCSV.add(new File(noarFileOutput));
//                        File patientFolder = new File(sourceFile).getParentFile();
//                        Boolean deleteValue = deleteDir(patientFolder);
//                        if(deleteValue){
//                            System.out.println(LocalDateTime.now()+": generate noar csv file successful: "+ noarFileOutput);
//                            System.out.println(LocalDateTime.now()+": delete folder successful: "+ patientFolder.getName());
//                        }else{
//                            setLog(LocalDateTime.now()+": generate both csv file successful, but delete folder failed: "+ patientFolder.getName());
//                        }
                    }else{
                        setLog(LocalDateTime.now()+": generate noar csv file failed: "+ noarFileOutput);
                    }

                }else{
                    setLog(LocalDateTime.now()+": generate ar csv file failed: "+ arFileOutput);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

//        File[] patientFolders = new File(EEGDIRECTORY).listFiles();
//        for (File patientFolder : patientFolders){
//            if(patientFolder.isDirectory() && patientFolder.list().length == 0){
//                patientFolder.delete();
//            }
//        }

        return successfulCSV;
    }

    private int switchRegistry(String fileType){
        String value;
        String entry = "HKEY_CURRENT_USER\\Software\\Persyst\\PSMMarker\\Settings";
        String parameter = "_ForceRawTrends";

        if(fileType.equals("ar")){
            value = "0x00000000";
        }else {
            value = "0x00000001";
        }

        try{
            Runtime.getRuntime().exec("reg add " + entry + " /v " + parameter + " /t REG_DWORD /d " + value + " /f");
            setLog(LocalDateTime.now()+": change registry to generate "+ fileType + " successful !" );
            return 0;
        }catch (Exception e){
            setLog(LocalDateTime.now()+": change registry to generate "+ fileType + " failed !" );
            return 1;
        }
    }

    private void sendCSVFiles(List<File> fileList){
        for(File file: fileList){
            //scp
            try {
                setLog(LocalDateTime.now()+"start sending file " + file.getName());
                // create txt to represent finish
                File finishMark = new File(file.getAbsolutePath().replace(".csv",".txt"));
                finishMark.createNewFile();
                if(scpSend(file.getAbsolutePath()) && scpSend(finishMark.getAbsolutePath()) && finishMark.delete() && moveFile(FINISHEDFILES,file)){
                    setLog(LocalDateTime.now()+": finish sending file "+ file.getName());
                }
            }catch (Exception ee){
                setLog(LocalDateTime.now()+": sending file failed "+ file.getName());
            }
        }
        System.out.println("finished");
    }

    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }



    private void setLog(String log){
        try {
            File writename = new File(LOGPATH);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(writename, true)));
            out.write(log+"\n");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setLog2(String log,String path){
        try {
            File writename = new File(path);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(writename, true)));
            out.write(log+"\n");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean moveFile(String path, File file){
        File folder = new File(path);
        String year = file.getName().substring(4,8);
        String pid = file.getName().substring(0,12);
        Queue<File> fileQueue = new LinkedList<>();
        if(folder.listFiles()!=null){
            for(File f: folder.listFiles()){
                if(f.getName().equals(year)){
                    fileQueue.add(f);
                }

            }
        }
        while (!fileQueue.isEmpty()){
            File current = fileQueue.poll();
            if(current.getName().equals(year)){
                for(File f: current.listFiles()){
                    fileQueue.add(f);
                }
            }else if(current.getName().equals(pid)){
               return file.renameTo(new File(current.getPath()+"/"+file.getName()));
            }
        }
        File newFolder = new File(path+"/"+year);
        newFolder.mkdir();
        File subFolder = new File(path+"/"+year+"/"+pid);
        subFolder.mkdir();
        return file.renameTo(new File(subFolder.getPath()+"/"+file.getName()));

    }

    public List<File> getAllCSV(String dir){
        List<File> toBeSent = new ArrayList<>();
        File folder = new File(dir);
        File[] files = folder.listFiles();
        for(File f: files){
            if(f.isFile() && f.getName().toLowerCase().endsWith(".csv")){
                toBeSent.add(f);
            }
        }
        return toBeSent;
    }

    public static void main(String args[]) throws IOException {
//        try{
//            Properties pps = new Properties();
//            InputStream in = new BufferedInputStream(new FileInputStream(
//                    new File("config.properties")));
//            pps.load(in);
//            AutoStreaming autoScp = new AutoStreaming(pps.getProperty("IP"),Integer.parseInt(pps.getProperty("PORT")),pps.getProperty("USER"),
//                    pps.getProperty("PASSWORD"),pps.getProperty("PANEL"),pps.getProperty("MMXFILE"),pps.getProperty("EEGDIRECTORY"),
//                    pps.getProperty("CSVDIRECTORY"),pps.getProperty("PSCLIDIRCTORY"),pps.getProperty("DESTINATION"),pps.getProperty("FINISHEDFILES"),pps.getProperty("LOGPATH"));
//            autoScp.cronJob(19,0,0);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        ProducerCSVService producerCSVService = new ProducerCSVService();
//        AutoStreaming autoStreaming=new AutoStreaming() ;
//        List<File> fileList= autoStreaming.getAllCSV("C:\\Users\\Administrator\\Desktop\\kafkaTest");
//        for (int n=0;n<fileList.size();n++){
//            System.out.println( fileList.get(n));
//        }
//        AutoStreaming autoStreaming=new AutoStreaming();
//        File file=new File("C:\\Users\\Administrator\\Desktop\\kafkaTest\\PUH-2015-015_09ar.csv");
//        autoStreaming.moveFile("C:\\Users\\Administrator\\Desktop\\kafkaTest",file);



    }
}

