function mutaElement($sursa, destinatieId) {
    const $destinatie = $('#' + destinatieId);
  
    $sursa.find('option:selected').each(function () {
      const $optiune = $(this);
      const $nouOpt = $('<option>', {
        text: $optiune.text(),
        value: $optiune.val()
      });
  
      $destinatie.append($nouOpt);
      $optiune.remove();
    });
}