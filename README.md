# concordion-json-compare-plugin
A JSON comparison extension for Concordion.

You can find this project and its original author on the project page https://github.com/ThetaSinner/concordion-json-compare-plugin

The plugin compares JSON and appends a list of differences (if any) to the concordion HTML output.

Comparing JSON rather than text allows the order of fields to vary and formatting to be ignored.
It is much quicker (hopefully) to read the diff which is produced by the plugin than it would be to compare two JSON documents
manually.
