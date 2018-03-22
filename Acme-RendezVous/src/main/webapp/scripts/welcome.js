function getMessage(wholeMessage, language) {
	var value = "|" + wholeMessage;
	var parts = value.split("|" + language + "=");

	if (parts.length == 2)
		return parts.pop().split("|").shift();
	else
		return "";
}
