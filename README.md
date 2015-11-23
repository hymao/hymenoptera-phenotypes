## Running 

The current release has the phenotype-to-HTML-table script as the default behavior.

Download the latest release as `tgz` archive from https://github.com/hymao/hymenoptera-phenotypes/releases

Unzip, then execute the `hymenoptera-phenotypes` script found within the `bin` folder. It accepts one argument, the path to an OWL file. It outputs an HTML table to standard out. You can redirect the output to a file like so:

`./bin/hymenoptera-phenotypes phenotypes.owl >phenotypes.html`
