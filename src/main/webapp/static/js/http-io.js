"use strict";
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
            .success(
              function(template) {
                  var payload = template;
    
                  // write user input to the xml
                  $.each(map, function(key, val){
                        $(payload).find(key).text(val);
                  })
                  
                  putRawXml(getUri(), toString(payload));
            })
            .error(function(data) {
              // TODO implement notification
              alert('error: ' + data);
            })
        return false;
  });
});

function toString(xmlDocument) {
    if (typeof XMLSerializer != "undefined" && !($.browser.msie && parseInt($.browser.version) == 9)) {
         return (new XMLSerializer()).serializeToString(xmlDocument);
    }
    else {
        return xmlDocument.xml;
    }
}

function putRawXml(uri, xml) {
    $.ajax({
        type: "PUT",
        dataType: "application/xml",
        contentType: "application/xml",
        url: uri,
        data: xml 
    })
    .success(function(msg){
        alert('The response was: ' + msg.status + ', ' + msg.responseText);
    })
    .error(function(msg){
        alert('The response was: ' + msg.status + ', ' + msg.responseText);
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
    var selectedGenres=map['genres'];
    var genreSize= selectedGenres.length;
    if(genreSize) {
      $root.find('pubman-admin-descriptor')
        .append($('<allowed-genres />'));
      $.each(selectedGenres, function(index, genre){
          $root.find('allowed-genres')
            .append($('<allowed-genre />').text(genre.value));
      });
    } 

    //FIXME refactor to function
    var selectedSubjects=map['subjects'];
    if(selectedSubjects.length) {
      $root.find('pubman-admin-descriptor')
        .append($('<allowed-subject-classifications />'));
      $.each(selectedSubjects, function(index, subject){
        $root.find('allowed-subject-classifications')
          .append($('<allowed-subject-classsification />').text(subject.value));
      });
    } 

    //FIXME refactor to function
    $root.find('pubman-admin-descriptor')
      .append($('<validation-schema />').text(map['schema'].val()));

    //FIXME refactor to function
    $root.find('pubman-admin-descriptor')
      .append($('<workflow />').text(map['workflow'].val()));

    //FIXME refactor to function
    if(map['email'].val()){
      $root.find('pubman-admin-descriptor')
        .append($('<contact-email />').text(map['email'].val()));
    } 

    putRawXml(getUri(), $root.html());
    return false;
  });
});

function serializePubmanContextForm(){
  var map={};
  map['genres']= $('input[name="genresList"]:checked');
  map['subjects']=$('input[name="subjectList"]:checked');
  map['workflow']=$('#workflow option:selected');
  map['schema']=$('#validation-schema option:selected');
  map['email']=$('input[name="contact-email"]');
  return map;
}

// Raw Metadata
// TODO rewrite with jQuery
function sendRawXml() {
  var userInput = document.getElementById('content').value;
  var uri = getUri();
  putRawXml(getUri(), userInput);
  return false;
}

// TODO rewrite with jQuery
function getUri() {
  return window.location.href;
}

function put(xml) {
  putRawXml(getUri(),xml);
}