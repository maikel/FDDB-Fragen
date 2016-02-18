#!/bin/bash

# dont let the script being started from the terminal. we want to pipe files
# into it.
if [ -t 0 ]; then
	echo "Do not call this script from terminal please.";
else
	# the first line is empty if and only if there are no new questions and
	# answers. check if it is empty and exit the script it is.
	read line;
	if [[ -z $line ]]; then
		exit 0;
	fi

	# read the hole pipe or file before mailing the content
	input=$line;
	while read line; do
		input=$input$line;
	done < /dev/stdin

	# mail the content
	echo $input | /usr/bin/mail \
		-a "From: <sender-email>" \
		-a "MIME-Version: 1.0" \
		-a "Content-Type: text/html; charset=UTF-8" \
		-s "[FDDB] Neue Nachrichten" <recipient-email>
fi

