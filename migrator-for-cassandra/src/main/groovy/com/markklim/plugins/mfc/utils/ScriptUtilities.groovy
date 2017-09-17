package com.markklim.plugins.mfc.utils

class ScriptUtilities {
    static String getScriptFromFile(File file) {
        file.getText('UTF-8')
                .with { removeComments(it) }
    }

    static String removeComments(String script) {
        script.replaceAll(/\/\/.*/, '')
    }

    static String implementPlaceholders(Map parameters, String script) {
        parameters.placeholders
                .each { script = script.replace("%${it.key}%", it.value as String) }
        script
    }

    static List scriptToList(String script) {
        script.trim()
                .replace('\r', '')
                .replace('\n', '')
                .split(';')
                .findAll { it != '' }
    }
}
