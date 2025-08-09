package com.tars;

import javax.swing.*;

public class CurrentPathStrategy implements FileChooserPathStrategy {

    private final String currentPath;

    public CurrentPathStrategy(String currentPath) {
        this.currentPath = currentPath;
    }

    @Override
    public JFileChooser createFileChooser() {
        return new JFileChooser(currentPath);
    }
}
