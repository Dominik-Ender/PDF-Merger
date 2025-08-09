package com.tars;

import com.tars.strategy.CurrentPathStrategy;
import com.tars.strategy.FileChooserPathStrategy;
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

//        JFrame frame = new JFrame(PDFMERGER);
        JFrame frame = new StyledFrame(PDFMERGER);

        JButton jAddFileButton, jRemoveFileButton, jMergeFilesButton, jClearListButton;
        JScrollPane jScrollPane;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel();
        JPanel midPanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        BoxLayout boxLayoutTop = new BoxLayout(topPanel, BoxLayout.Y_AXIS);
        BoxLayout boxLayoutCenter = new BoxLayout(midPanel, BoxLayout.Y_AXIS);
        BoxLayout boxLayoutBottom = new BoxLayout(bottomPanel, BoxLayout.Y_AXIS);

        topPanel.setLayout(boxLayoutTop);
        midPanel.setLayout(boxLayoutCenter);
        bottomPanel.setLayout(boxLayoutBottom);

        topPanel.setBorder(new EmptyBorder(new Insets(10, 20, 10, 15)));
        midPanel.setBorder(new EmptyBorder(new Insets(10, 15, 10, 15)));
        bottomPanel.setBorder(new EmptyBorder(new Insets(10, 20, 10, 15)));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> fileList = new JList<>(listModel);
        jScrollPane = new JScrollPane(fileList);
        jScrollPane.setPreferredSize(new Dimension(550, 200));
        jScrollPane.setUI(new StyledScrollPaneUI(15, Color.GREEN, Color.BLACK));

        jAddFileButton = new JButton(ADDFILEBUTTONNAME);
        jAddFileButton.setFont(new Font("Calibri", Font.PLAIN, 14));
        jAddFileButton.setBackground(new Color(0x2dce98));
        jAddFileButton.setUI(new StyledButtonUI());
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
        jRemoveFileButton.setUI(new StyledButtonUI());
        jRemoveFileButton.setBackground(new Color(0x2dce98));
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
        jMergeFilesButton.setUI(new StyledButtonUI());
        jMergeFilesButton.setBackground(new Color(0x2dce98));
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
        jClearListButton.setUI(new StyledButtonUI());
        jClearListButton.setBackground(new Color(0x2dce98));
        jClearListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!listModel.isEmpty()) {
                    listModel.clear();
                }
            }
        });

        Dimension buttonSize = new Dimension(120, 30);
        jAddFileButton.setPreferredSize(buttonSize);
        jRemoveFileButton.setPreferredSize(buttonSize);
        jClearListButton.setPreferredSize(buttonSize);
        jMergeFilesButton.setPreferredSize(buttonSize);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(10,10,10,10));
        frame.setContentPane(content);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);

// Zeile 0 – drei Buttons nebeneinander
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        content.add(jAddFileButton, gbc);

        gbc.gridx = 1;
        content.add(jRemoveFileButton, gbc);

        gbc.gridx = 2;
        content.add(jClearListButton, gbc);

// Zeile 1 – ScrollPane über ganze Breite
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;              // über alle Spalten
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;               // ScrollPane füllt vertikal aus
        content.add(jScrollPane, gbc);

// Zeile 2 – Merge-Button

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.WEST;
        gbc.weighty = 0;
        content.add(jMergeFilesButton, gbc);


//        topPanel.add(jAddFileButton, BorderLayout.NORTH);
//        topPanel.add(jRemoveFileButton, BorderLayout.NORTH);
//        topPanel.add(jClearListButton, BorderLayout.NORTH);
//
//        midPanel.add(jScrollPane, BorderLayout.CENTER);
//
//        bottomPanel.add(jMergeFilesButton);
//
//        frame.add(topPanel, BorderLayout.NORTH);
//        frame.add(midPanel, BorderLayout.CENTER);
//        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.pack();

        frame.setVisible(true);
    }
}