package com.markklim.plugins.mfc.utils

class ScriptUtilities {
    static String getScriptFromFile(File file) {
        file.getText('UTF-8')
                .with { removeComments(it) }
    }

    static String removeComments(String script) {
        script.replaceAll(/\/\/.*/, '')
    }

    static String implementPlaceholders(String script) {
        script.replace("%schema%", 'tda')
    }

    static List scriptToList(String script) {
        script.trim()
                .replace('\r', '')
                .replace('\n', '')
                .split(';')
                .findAll { it != '' }
    }
}
