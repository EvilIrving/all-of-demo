/**************************************************************************
	Copyright (c) 2001 Geir Landrö (drop@destroydrop.com)
	Checkbox Script - www.destroydrop.com/javascripts/checkbox/
	Version 1.0	

	This script can be used freely as long as all copyright messages are
	intact.
**************************************************************************/

// Count number of checked items
function numChecked(f) {
	form = document.getElementById(f);
	j=0;
	for(i=0;i< form.length;i++) {
		e=form.elements[i];
		if (e.type=="checkbox" && e.name != "selAll" && e.checked)
			j++;	
	}
	return j;
}
// Count number of checkboxes
function numBoxes(f) {
	form = document.getElementById(f);
	j=0;
	for(i=0;i< form.length;i++) {
		e=form.elements[i];
		if (e.type=="checkbox" && e.name != "selAll")
			j++;	
	}
	return j;
}
// Call this when item is checked/unchecked
function selOne(f) {
	form = document.getElementById(f);
	if (numChecked(f) == numBoxes(f)) form.selAll.checked=true;
	else form.selAll.checked=false;
}
// Call this when All items checkbox is checked/unchecked
function selAllItems(f) {
	form = document.getElementById(f);
	for(i=0;i< form.length;i++) {
		e=form.elements[i];
		if (form.selAll.checked) {
			if (e.type=="checkbox" && e.name != "selAll" && !e.checked) e.checked=true;
		} else {
			if (e.type=="checkbox" && e.name != "selAll" && e.checked) e.checked=false;	
		}
	}
}
// Call this when the form is submitted
function formSubmit(f) {
	form = document.getElementById(f);
	if (numChecked(f) == 0) {
		alert("No items checked!");
	} else {
		if (form.selAll.checked) {
			if (confirm("All items selected!\nSubmit?"))
				form.submit();    
		} else {
			form.submit();
		}
	}
}
