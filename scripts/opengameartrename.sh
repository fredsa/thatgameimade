#!/bin/bash

if [ $# != 2 ]
then
  echo
  echo "Usage:"
  echo "  $0 <dirname> <prefix>"
  echo
  echo "e.g."
  echo "  $0 app/src/main/res/drawable-nodpi/ icepack"
  echo
  exit
fi

(cd $1
  pwd
  for file in $( ls *.png | egrep '[A-Z]' )
  do
    newname="$( echo $file | sed 's/\([A-Z\]\)/_\1/g' | tr 'A-Z' 'a-z' )"
    git mv $file $2_$newname 2>/dev/null || mv $file $2_$newname
  done
)
