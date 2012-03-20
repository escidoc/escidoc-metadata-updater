function checkAll(field){
  for (i = 0; i < field.length; i++){
    field[i].checked = true ;
  }
}

function uncheckAll(field){
  for (i = 0; i < field.length; i++){
    field[i].checked = false ;
  }
}

function getDescriptor() {
  $('.actions').click(function(e) {
    e.preventDefault();
  });
  $
  .ajax({
    type : "GET",
    url : "http://localhost:8082/rest/v0.9/contexts/escidoc:136/metadata/pubman?escidocurl=http://esfedrep1.fiz-karlsruhe.de:8080",
    dataType : "xml",
    success : function(xml) {
      XMLDocument = xml;
      readXML();
    }
  });
}

/*
 * function put(xml) { $.ajax({ type : "PUT", url :
 * "http://localhost:8082/rest/v0.9/contexts/escidoc:136/metadata/pubman?escidocurl=http://esfedrep1.fiz-karlsruhe.de:8080",
 * dataType : "xml", data : xml }); }
*/

function put(xml) {
  var TEST_URI = "http://localhost:8082/rest/v0.9/contexts/escidoc:136/metadata/pubman?escidocurl=http://esfedrep1.fiz-karlsruhe.de:8080";
  var xhr2 = new XMLHttpRequest();
  xhr2.open('PUT', TEST_URI, false);
  xhr2.setRequestHeader('Accept', 'application/xml;charset=UTF-8');
  xhr2.setRequestHeader('Content-Type', 'application/xml;charset=UTF-8');

  xhr2.onreadystatechange = function(event) {
    if (xhr2.readyState === 4) {
      if (xhr2.status === 200) {
        var r = xhr2.responseXML;
        console.log("Successfully update the metadata.");
        console.log(r);
        alert('The response was: ' + xhr2.status + ', '
              + xhr2.responseText);
      } else if (xhr2.status === 303) {
        alert('redirect');
      } else if (xhr2.status === 0) {
        alert('zero...');
      } else {
        console.log("status: ", xhr2.statusText);
        alert('Snap, something went wrong: ' + xhr2.status
              + ', reason: ' + xhr2.responseText);
      }
    }
  };
  xhr2.send(xml);
}

function readXML() {
  var genresList = $(XMLDocument).find("allowed-genre");
  var subjectList = $(XMLDocument).find("allowed-subject-classification");
  var contactMail = $(XMLDocument).find("contact-email");
  var validationSchema = $(XMLDocument).find("validation-schema");
  var workflow = $(XMLDocument).find("workflow");
  for (i = 0; i < genresList.length; i++) {
    if (genresList[i].textContent != "") {
      $('input[name="genresList"]').attr('checked', true);
    }
  }
  for (j = 0; j < subjectList.length; j++) {
    if (subjectList[j].textContent != "") {
      $('input[name="subjectList"]').attr('checked', true);
    }
  }
  $('input[name="contact-email"]').val(contactMail[0].textContent);
  $('#validation-schema option:selected').text(
    validationSchema[0].textContent);
    $('#workflow option:selected').text(workflow[0].textContent);
}

function deleteXML() {
  $(XMLDocument).find("pubman-admin-descriptor").each(
    function() {
    var allowedGenre = $(XMLDocument).find("allowed-genre");
    var allowedSubjectClassifications = $(XMLDocument).find(
      "allowed-subject-classification");
      for (i = 0; i < allowedGenre.length; i++) {
        allowedGenre[i].textContent = "";
      }
      for (j = 0; j < allowedSubjectClassifications.length; j++) {
        allowedSubjectClassifications[j].textContent = "";
      }
  });
}

function adjustValues() {
  // get all checked Checkboxes and input values
  var genresList = $('input[name="genresList"]:checked');
  var subjectList = $('input[name="subjectList"]:checked');
  var contactMail = $('input[name="contact-email"]');
  var workflow = $("#workflow");
  var validationSchema = $("#validation-schema");
  deleteXML();
  for (i = 0; i < genresList.length; i++) {
    var allowedGenre = $(XMLDocument).find("allowed-genre");
    allowedGenre[i].textContent = genresList[i].value;
  }
  for (j = 0; j < subjectList.length; j++) {
    var allowedSubjectClassifications = $(XMLDocument).find(
      "allowed-subject-classification");
      allowedSubjectClassifications[j].textContent = subjectList[j].value;
  }

  $(XMLDocument).find("contact-email").text(contactMail.val());
  $(XMLDocument).find("validation-schema").text(validationSchema.val());
  $(XMLDocument).find("workflow").text(workflow.val());

  put(XMLDocument);
}

function updateDescriptor() {
  $('.actions').click(function(e) {
    e.preventDefault();
  });
  adjustValues(XMLDocument);
}
