function getGenresList() {
    var genresList = document.getElementsByName('genresList');
    return genresList;
}
function getSubjectList() {
    var subjectList = document.getElementsByName('subjectList');
    return subjectList;
}
function getMailAddress() {
    var mailAddress = document.getElementsByName('contact-email');
    return mailAddress;
}
function getWorkflow() {
    var workflow = document.getElementById("workflow");
    //var workflow = elementID.options[elementID.selectedIndex].value;
    return workflow;
}
function getValidationSchema() {
    var validationSchema = document.getElementById("validation-schema");
    //var validationSchema = elementID.options[elementID.selectedIndex].value;
    return validationSchema;
}
function getXMLHttpRequest() {
    return new XMLHttpRequest();
}
function getDescriptor() {
    var xhr = getXMLHttpRequest();
    xhr.open('GET', 'pubman-admin-descriptor.xml', false);
    xhr.setRequestHeader('Accept', 'application/xml;charset=UTF-8');
    xhr.setRequestHeader('Content-Type', 'application/xml;charset=UTF-8');
    xhr.send(null);
    var responseXML = xhr.responseXML;
    return responseXML;
}

function put(xml) {
    var xhr2 = new XMLHttpRequest();
    xhr2.open('PUT', 'pubman-admin-descriptor.xml', false);
    xhr2.setRequestHeader('Accept', 'application/xml;charset=UTF-8');
    xhr2.setRequestHeader('Content-Type', 'application/xml;charset=UTF-8');
    xhr2.send(null);
    var responseXML = xhr2.responseXML;
    console.log("Successfully update the metadata.");
    console.log(responseXML);
        
    xhr2.send(xml);
}

function adjustValues(list, rXml) {
    if(list.localName == 'select') {
        var elementValue = list.options[list.selectedIndex].value;
        var elementTag = rXml.getElementsByTagName(list.id);
        if(elementTag != null) {
            var elementTagValue = elementTag[0].textContent;
            if(elementValue != elementTagValue) {
                elementTag[0].textContent = elementValue;
            }
        }
    }
    if(list.length == 1) {
        if(list[0].name == 'contact-email') {
            var elementValue = list[0].value;
            var elementTag = rXml.getElementsByTagName(list[0].name);
            if(elementTag != null) {
                var elementTagValue = elementTag[0].textContent;
                if(elementValue != elementTagValue) {
                    elementTag[0].textContent = elementValue;
                }
            }
        }
    }
    
    Array.prototype.forEach.call(list, function(li, index, nodeList) {
        var found;
        if (li.type !== 'button') {
            if(li.checked) {
                if(li.name == 'genresList') {
                    found = rXml.getElementsByTagName('allowed-genre');
                    if (found != null) {
                        var input = found[0];
                        input.textContent = li.value;
                    }
                }
                else if(li.name = 'subjectList') {
                    found = rXml.getElementsByTagName('allowed-subject-classification');
                    if (found != null) {
                        var input = found[0];
                        input.textContent = li.value;
                    }
                }
            }
        }
    });
    put(rXml);
}
function updateDescriptor() {
    var rXML = getDescriptor();
    var genresList = getGenresList();
    var validationSchema = getValidationSchema();
    var workflow = getWorkflow();
    var subjectList = getSubjectList();
    var mailAddress = getMailAddress();
    adjustValues(genresList, rXML);
    adjustValues(validationSchema, rXML);
    adjustValues(workflow, rXML);
    adjustValues(subjectList, rXML);
    adjustValues(mailAddress, rXML);
}
