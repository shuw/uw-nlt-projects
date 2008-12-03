#!/bin/bash

java -Xmx512m -cp bin project2.Search generated/vocabulary.txt generated/clusters.txt ~/dropbox/08-09/570/project2/files2 < input/queries.txt