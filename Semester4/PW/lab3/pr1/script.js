function mutaElement(sursa, destinatieId) {
    const destinatie = document.getElementById(destinatieId);
    const optiuniSelectate = Array.from(sursa.options).filter(opt => opt.selected);

    for (let opt of optiuniSelectate) {
      const nouOpt = new Option(opt.text, opt.value);
      destinatie.add(nouOpt);
      sursa.remove(opt.index);
    }
  }