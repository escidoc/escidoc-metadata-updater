function serializePubmanContextForm(){
  var map={};
  map['genres']= $('input[name="genresList"]:checked');
  map['subjects']=$('input[name="subjectList"]:checked');
  map['workflow']=$('#workflow option:selected');
  map['schema']=$('#validation-schema option:selected');
  map['email']=$('input[name="contact-email"]');
  return map;
}

$(function() {
  $('#pubman-context-metadata-editor').submit(
    function(e) {
    e.preventDefault();

    var map = serializePubmanContextForm();
    $.get('/rest/pubman-context-metadata-template.xml').success(
      function(template) {
      var payload = template;

      // when the user does not select any genres, remove the element the node '<allowed-genres/>'
      // other wise, add the selected genres as: <allowed-genre>$(value)</allowed-genre>
      var selectedGenres=map['genres']; 
      var genreSize= selectedGenres.length;
      if(genreSize) {
        $.each(selectedGenres, function(index, genre){
          $(payload).find('allowed-genres').append($('<allowed-genre>').text(genre.value)); 
        });
      } else{
        $(payload).find('allowed-genres').remove();
      }


      //FIXME refactor to function
      var selectedSubjects=map['subjects']; 
      if(selectedSubjects.length) {
        $.each(selectedSubjects, function(index, subject){
          $(payload).find('allowed-subject-classifications').append($('<allowed-subject-classification>').text(subject.value)); 
        });
      } else{
        $(payload).find('allowed-subject-classification').remove();
      }

      var validationSchema = $(payload).find('validation-schema');
      validationSchema.text(map['schema'].val());

      var workflow= $(payload).find('workflow');
      workflow.text(map['workflow'].val());

      var contactEmail= $(payload).find('contact-email');
      contactEmail.text(map['email'].val());

      putRawXml(getUri(), payload);
    }).error(function(data) {
      // TODO implement notification
      alert('error: ' + data);
    })
    return false;
  });
});


//TODO When the user clicks the save button, she should be able to update the pubman context metadata in the escidoc core.
//TODO 1. read the user input that changes
//TODO 2. collect them
//what to collect:
// 1. Genres 			multiple	
// 2. Subjects			multiple
// 3. Workflow			single
// 4. Validation Schema single
// 5. Contact Email 	single
//TODO 3. serialize the values to XML
//TODO    a. write the xml using JavaScript
//TODO    b. load a xml template, remove unneccassry element