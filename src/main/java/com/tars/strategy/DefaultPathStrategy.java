package com.tars.strategy;

import javax.swing.*;

public class DefaultPathStrategy implements FileChooserPathStrategy {

    private final String defaultPath;

    public DefaultPathStrategy(String defaultPath) {
        this.defaultPath = defaultPath;
    }

    @Override
    public JFileChooser createFileChooser() {
        return new JFileChooser(defaultPath);
    }
}
