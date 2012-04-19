'use strict';
//TODO general todo: introduce nama spacing and object-oriented JavaScript

// PubMan Organization Metadata/Admin Descriptor
$(function() {
  $('#pubman-organization-metadata-editor')
      .submit(function(e) {
        e.preventDefault();

        // read user input from the form
        var map = $.serializeForm();

        // build PubMan Metadata XML
        $.get('/rest/pubman-organization-metadata-template.xml')
            .done(function(template) {
                  var payload = template;
    
                  // write user input to the xml
                  $.each(map, function(key, val){
                        $(payload).find(key).text(val);
                  });
                  
                  putRawXml(getUri(), toString(payload));
            })
            .fail(function(request, error) {
              $("#fail-message").fadeIn("slow");
              $("#fail-message a.close-notify").click(function() {
                    $("#fail-message").fadeOut("slow");
                });
            });
        return false;
  });
});

function toString(xmlDocument) {
    if (typeof XMLSerializer !== "undefined" && !($.browser.msie && parseInt($.browser.version, 10) == 9)) {
      return (new XMLSerializer()).serializeToString(xmlDocument);
    }
    else {
        return xmlDocument.xml;
    }
}

	
function putRawXml(uri, xml) {
	var request = $.ajax({
        type: "PUT",
        contentType: "application/xml",
        processData: false,
        url: uri,
        data: xml
     })
    .done(function(msg) {   
      $("#success-message").fadeIn("slow");
      $("#success-message a.close-notify").click(function() {
        $("#success-message").fadeOut("slow");
      });
      back();
    })
    .fail(function(request, error) {
      $("#fail-message").fadeIn("slow");
      $("#fail-message a.close-notify").click(function() {
            $("#fail-message").fadeOut("slow");
        });
    });
}

jQuery.extend({
  serializeForm : function() {
    var fields = $(":input").serializeArray();

    var map = {};
    $.each(fields, function(index, field) {
      map[field.name] = field.value;
    });

    return map;
  }
});

// PubMan Context Metadata/Admin Descriptor
$(function() {
  $('#pubman-context-metadata-editor').submit(
    function(e) {
    e.preventDefault();

    var map = serializePubmanContextForm();

    // create an XML Document
    var $root = $('<XMLDocument/>');
    $root
      .append($('<pubman-admin-descriptor />'));

    // when the user does not select any genres, remove the element the node '<allowed-genres/>'
    // other wise, add the selected genres as: <allowed-genre>$(value)</allowed-genre>
    //FIXME refactor to function
    var selectedGenres=map.genres;
    var genreSize= selectedGenres.length;
    $root.find('pubman-admin-descriptor')
      .append($('<allowed-genres />'));
    
    if(genreSize) {
      $.each(selectedGenres, function(index, genre){
          $root.find('allowed-genres')
            .append($('<allowed-genre />').text(genre.value));
      });
    } 

    //FIXME refactor to function
    $root.find('pubman-admin-descriptor')
      .append($('<allowed-subject-classifications />'));
    
    var selectedSubjects=map.subjects;
    if(selectedSubjects.length) {
      $.each(selectedSubjects, function(index, subject){
        $root.find('allowed-subject-classifications')
          .append($('<allowed-subject-classification />').text(subject.value));
      });
    } 

    //FIXME refactor to function
    $root.find('pubman-admin-descriptor')
      .append($('<validation-schema />').text(map.schema.val()));

    //FIXME refactor to function
    $root.find('pubman-admin-descriptor')
      .append($('<workflow />').text(map.workflow.val()));

    //FIXME refactor to function
    if(map.email.val()){
      $root.find('pubman-admin-descriptor')
        .append($('<contact-email />').text(map.email.val()));
    } 

    putRawXml(getUri(), $root.html());
    return false;
  });
});

function serializePubmanContextForm(){
  var map={};
  map.genres = $('input[name="genresList"]:checked');
  map.subjects =$('input[name="subjectList"]:checked');
  map.workflow =$('#workflow option:selected');
  map.schema =$('#validation-schema option:selected');
  map.email =$('input[name="contact-email"]');
  return map;
}

// Raw Metadata

$(function() {
  $('#raw-xml-metadata-editor').submit(
    function(e) {
    e.preventDefault();
    putRawXml(getUri(), $('#content').val());
    return false;
  });
});

// TODO rewrite with jQuery
function getUri() {
  return window.location.href;
}

function put(xml) {
  putRawXml(getUri(),xml);
}

function back () {
    if(document.referrer === "") {
      //Do nothing.
      log('empty, tell user to close the Web Browser tab/window');
    } else{
      window.open(document.referrer,'_self');
    }
}
function log (aString) {
    if(typeof console !== "undefinded" && typeof console.log !== "undefinded"){
      console.log(aString);
    }  
}

$('#cancel').click(function () {
  back();
});
