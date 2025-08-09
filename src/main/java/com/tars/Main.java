package com.tars;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main extends JFrame {

    private final static String ADDFILEBUTTONNAME = "Add File";
    private final static String REMOVEFILEBUTTONAME = "Delete File";
    private final static String MERGEFILESBUTTONNAME = "Merge Files";
    private final static String USERDIRECTORY = "user.dir";
    private final static String PDFMERGER = "PDF-Merger";
    private final static String PDFEXTENSION = "pdf";
    private final static String PDFEXTENSIONDOT = ".pdf";
    private final static String PDFEXTENSIONDOTASTERISK = "*.pdf";
    private final static String PDFFILES = "PDF Files";
    private final static String CHOOSESAVELOCATION = "Choose save location";
    private final static String CLEARLISTBUTTONNAME = "Clear List";

    private static String currentPath = "";
    private static FileChooserPathStrategy fileChooserPathStrategy;

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception exception) {}

        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame(PDFMERGER);

        JButton jAddFileButton, jRemoveFileButton, jMergeFilesButton, jClearListButton;
        JScrollPane jScrollPane;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel();
        JPanel midPanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        BoxLayout boxlayout = new BoxLayout(topPanel, BoxLayout.X_AXIS);

        topPanel.setLayout(boxlayout);

        topPanel.setBorder(new EmptyBorder(new Insets(100, 150, 100, 150)));
        midPanel.setBorder(new EmptyBorder(new Insets(100, 150, 100, 150)));
        bottomPanel.setBorder(new EmptyBorder(new Insets(100, 150, 100, 150)));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> fileList = new JList<>(listModel);
        jScrollPane = new JScrollPane(fileList);

        jAddFileButton = new JButton(ADDFILEBUTTONNAME);
        jAddFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if(currentPath != null && !currentPath.isEmpty()) {
                    fileChooserPathStrategy = new CurrentPathStrategy(currentPath);
                } else {
                    fileChooserPathStrategy = new CurrentPathStrategy(USERDIRECTORY);
                }

                JFileChooser jFileChooser = fileChooserPathStrategy.createFileChooser();

                FileFilter fileFilter = new FileNameExtensionFilter(PDFFILES, PDFEXTENSIONDOT, PDFEXTENSIONDOTASTERISK, PDFEXTENSION);
                jFileChooser.addChoosableFileFilter(fileFilter);
                jFileChooser.setFileFilter(fileFilter);
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jFileChooser.setMultiSelectionEnabled(true);

                System.out.println("||| " + currentPath);

                if(jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File[] selectedFiles = jFileChooser.getSelectedFiles();

                    for(File file : selectedFiles) {
                        if(file.getName().toLowerCase().endsWith(PDFEXTENSIONDOT)) {
                            listModel.addElement(file.getAbsolutePath());
                        }
                    }
                }
                currentPath = jFileChooser.getCurrentDirectory().getAbsolutePath();
            }
        });

        jRemoveFileButton = new JButton(REMOVEFILEBUTTONAME);
        jRemoveFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = fileList.getSelectedIndex();
                if(selectedIndex != -1) {
                    listModel.remove(selectedIndex);
                }
            }
        });

        jMergeFilesButton = new JButton(MERGEFILESBUTTONNAME);
        jMergeFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!listModel.isEmpty()) {
                    try {
                        PDFMergerUtility pdfMerger = new PDFMergerUtility();

                        for(int i = 0; i < listModel.size(); i++) {
                            pdfMerger.addSource(new File(listModel.get(i)));
                        }

                        if(currentPath != null && !currentPath.isEmpty()) {
                            fileChooserPathStrategy = new CurrentPathStrategy(currentPath);
                        } else {
                            fileChooserPathStrategy = new CurrentPathStrategy(USERDIRECTORY);
                        }

                        JFileChooser jSaveChooser = fileChooserPathStrategy.createFileChooser();

                        jSaveChooser.setDialogTitle(CHOOSESAVELOCATION);

                        FileFilter fileFilter = new FileNameExtensionFilter(PDFFILES, PDFEXTENSIONDOT, PDFEXTENSIONDOTASTERISK, PDFEXTENSION);
                        jSaveChooser.addChoosableFileFilter(fileFilter);
                        jSaveChooser.setFileFilter(fileFilter);
                        jSaveChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                        if(jSaveChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {

                            File choosenFile = jSaveChooser.getSelectedFile();
                            String destinationPath = "";

                            if(choosenFile.getName().trim().isEmpty()) {
                                destinationPath = new File(listModel.get(0)).getName();
                            } else {
                                destinationPath = choosenFile.getAbsolutePath();
                                if(!destinationPath.endsWith(PDFEXTENSIONDOT)) {
                                    destinationPath += PDFEXTENSIONDOT;
                                }
                            }

                            pdfMerger.setDestinationFileName(destinationPath);

                            pdfMerger.mergeDocuments(null);

                            currentPath = jSaveChooser.getCurrentDirectory().getAbsolutePath();
                        }
                    } catch(Exception exception) {
                    }
                }
            }
        });

        jClearListButton = new JButton(CLEARLISTBUTTONNAME);
        jClearListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!listModel.isEmpty()) {
                    listModel.clear();
                }
            }
        });

        topPanel.add(jAddFileButton);
        topPanel.add(jRemoveFileButton);
        topPanel.add(jClearListButton);

        midPanel.add(jScrollPane, BorderLayout.CENTER);

        bottomPanel.add(jMergeFilesButton);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(midPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.pack();

        frame.setVisible(true);
    }
}