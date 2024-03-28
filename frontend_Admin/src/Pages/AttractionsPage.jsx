import React from 'react';

class AttractionsPage extends React.Component {
  state = {
    attractions: [], // Hier werden die Attraktionsdaten gespeichert
    allTags: [], // Hier werden alle verfügbaren Tags gespeichert
    selectedTags: [], // Hier werden die ausgewählten Tags gespeichert
    openingHoursChanges: {}, // Hier werden Änderungen an den Öffnungszeiten gespeichert
    filteredAttractions: [], // Hier werden die gefilterten Attraktionen gespeichert
    currentName: '', // Globaler Konstante für den aktuellen Namen
    newName: '', // Globaler Konstante für den neuen Namen
    newDescription: '', // Globaler Konstante für die neue Beschreibung
    weekday: '', // Globaler Konstante für den Wochentag
    startTimeHour: '', // Globaler Konstante für die Startzeit Stunde
    startTimeMinute: '', // Globaler Konstante für die Startzeit Minute
    endTimeHour: '', // Globaler Konstante für die Endzeit Stunde
    endTimeMinute: '', // Globaler Konstante für die Endzeit Minute
    tagToAdd: '', // Globaler Konstante für den hinzuzufügenden Tag
    tagToRemove: '', // Globaler Konstante für den zu entfernenden Tag
    attractionToDelete: '', //Attraktionen die gelöscht werden sollen
    newImageUrl: '', //Neue Url für das bild der Attraktion
    openingHourIdsToRemove: [], //Die zu löschenden Openinghours
  };

  //Empfangen der Daten der Tabelle
  componentDidMount() {
    fetch('http://localhost:8080/all-attractions/')
      .then(response => response.json())
      .then(data => {
        this.setState({ attractions: data });
        const allTags = [...new Set(data.flatMap(attraction => attraction.filterTagResponses))];
        this.setState({ allTags });
      })
      .catch(error => {
        console.error('Fehler beim Abrufen der Daten:', error);
      });
  }

  //Button zum sortieren der Tags wurde gedrückt
  handleTagButtonClick = (tag) => {
    if (this.state.selectedTags.includes(tag)) {
      this.setState(prevState => ({
        selectedTags: prevState.selectedTags.filter(selectedTag => selectedTag !== tag)
      }), this.filterAttractions);
    } else {
      this.setState(prevState => ({
        selectedTags: [...prevState.selectedTags, tag]
      }), this.filterAttractions);
    }
  }

  //Attraktionen werden nach gewählten tags sortiert
  filterAttractions = () => {
    if (this.state.selectedTags.length > 0) {
        const tagParams = this.state.selectedTags.map(tag => `tag=${tag}`).join('&');
        const url = `http://localhost:8080/attractions-by-tags/?${tagParams}`;
        fetch(url)
            .then(response => response.json())
            .then(data => {
                this.setState({ filteredAttractions: data });
            })
            .catch(error => {
                console.error('Fehler beim Abrufen der gefilterten Attraktionen:', error);
            });
    } else {
        this.setState({ filteredAttractions: this.state.attractions });
    }
  }

  //Update der felder die bearbeitet wurden
  handleAttractionChange(index, field, value) {
    const attractions = [...this.state.attractions];
    attractions[index][field] = value;
    this.setState({ attractions });
  }

  //opening hours werden bearbeitet
  handleOpeningHoursChange(attractionIndex, hourIndex, field, value) {
    this.setState(prevState => {
        const attractions = [...prevState.attractions];
        const attraction = { ...attractions[attractionIndex] };
        const openingHours = [...attraction.openingHoursResponses];
        openingHours[hourIndex][field] = value;
        if (field === 'startTime' || field === 'endTime') {
            const { hours, minutes } = this.splitTimeAttribute(value);
            attraction[field === 'startTime' ? 'startTimeHour' : 'endTimeHour'] = hours;
            attraction[field === 'startTime' ? 'startTimeMinute' : 'endTimeMinute'] = minutes;
        }
        attraction.openingHoursResponses = openingHours;
        attractions[attractionIndex] = attraction;
        return { attractions };
    });
}

// Methode zur Aufteilung der Zeitattribute
splitTimeAttribute(timeAttribute) {
    const [hours, minutes] = timeAttribute.split(':').map(part => parseInt(part));
    return { hours, minutes };
}

  //Tags werden bearbeiten
  handleTagChange(attractionIndex, tagIndex, newValue) {
    this.setState(prevState => {
      const attractions = [...prevState.attractions];
      const attraction = { ...attractions[attractionIndex] };
      const updatedTags = [...attraction.filterTagResponses];
      let filterTagsToAdd = attraction.filterTagsToAdd ? [...attraction.filterTagsToAdd] : [];
      let filterTagsToRemove = attraction.filterTagsToRemove ? [...attraction.filterTagsToRemove] : [];

      updatedTags[tagIndex] = newValue;

      if (!filterTagsToRemove.includes(attraction.filterTagResponses[tagIndex]) && newValue !== attraction.filterTagResponses[tagIndex]) {
        filterTagsToRemove.push(attraction.filterTagResponses[tagIndex]);
        filterTagsToAdd.push(newValue);
      } else if (filterTagsToRemove.includes(attraction.filterTagResponses[tagIndex]) && newValue === attraction.filterTagResponses[tagIndex]) {
        const indexToRemove = filterTagsToRemove.indexOf(attraction.filterTagResponses[tagIndex]);
        filterTagsToRemove.splice(indexToRemove, 1);
        const indexToAdd = filterTagsToAdd.indexOf(newValue);
        filterTagsToAdd.splice(indexToAdd, 1);
      }
      
      filterTagsToRemove = filterTagsToRemove.filter(tag => !filterTagsToAdd.includes(tag));

      attraction.filterTagResponses = updatedTags;
      attraction.filterTagsToAdd = filterTagsToAdd;
      attraction.filterTagsToRemove = filterTagsToRemove;
      attractions[attractionIndex] = attraction;
      return { attractions };
    });
  }


  //der Speichern button wurde gedrückt
  handleSaveClick(index) {
    const { attractions } = this.state;
    const attraction = attractions[index];

    if (!attraction.filterTagsToRemove) {
      attraction.filterTagsToRemove = [""];
    }

    attraction.filterTagResponses = attraction.filterTagResponses.filter(tag => tag !== "");

    const currentName = attraction.name;
  
    const newName = attraction.updatedName || attraction.name;
  
    const newDescription = attraction.description;
  
    let openingHours = [];
    if (attraction.openingHoursResponses.some(openingHour => openingHour.startTime !== '00:00:00' || openingHour.endTime !== '00:00:00')) {
      openingHours = attraction.openingHoursResponses.map(openingHour => ({
        weekday: openingHour.weekday,
        startTime: openingHour.updatedStartTime || openingHour.startTime,
        endTime: openingHour.updatedEndTime || openingHour.endTime,
        id: openingHour.id 
      }));
    } else {
      openingHours = attraction.openingHoursResponses;
    }
  
    const tags = attraction.filterTagResponses;
    const filterTagsToAdd = attraction.filterTagResponses; 
  
    const filterTagsToRemove = attraction.filterTagsToRemove;
    let startTimeHour = attraction.startTimeHour || '';
    let startTimeMinute = attraction.startTimeMinute || '';
    let endTimeHour = attraction.endTimeHour || '';
    let endTimeMinute = attraction.endTimeMinute || '';

    const existingWeekdays = new Map();
    this.state.attractions.forEach(attr => {
      attr.openingHoursResponses.forEach(openingHour => {
        existingWeekdays.set(openingHour.weekday, openingHour.id);
      });
    });
  
    const openingHourIdsToRemove = [];
    if (attraction.openingHoursResponses.some(openingHour => openingHour.weekday !== openingHours.weekday)) {
      const openingHoursResponses = attraction.openingHoursResponses;
      for (let i = 0; i < openingHoursResponses.length; i++) {
        const openingHour = openingHoursResponses[i];
        if (existingWeekdays.has(openingHour.weekday) && openingHour.weekday !== openingHours.weekday) {
          const idToAdd = openingHour.id;
          if (!openingHourIdsToRemove.includes(idToAdd)) {
            openingHourIdsToRemove.push(idToAdd); 
          }
        }
      }
    }
    
    const openingHourIdsToRemoveFromAttraction = attraction.openingHourIdsToRemove || [];
  
this.setState(prevState => ({
  currentName,
  newName,
  newDescription,
  openingHoursToAdd: openingHours,
  filterTagsToAdd,
  filterTagsToRemove: (attraction.filterTagsToRemove && attraction.filterTagsToRemove.length > 0) ? attraction.filterTagsToRemove : [""], 

  openingHourIdsToRemove: [...prevState.openingHourIdsToRemove, ...openingHourIdsToRemove] 
}), () => {
  this.updateAttractionData(attraction);
  //window.location.href = 'http://localhost:8082'
});
  }
  
  //wochentag der entfernt werden soll
  handleRemoveWeekday = (index, weekdayName) => {
    const { attractions } = this.state;
    const attraction = attractions[index];
  
    let weekdayIdToRemove = null;
    attraction.openingHoursResponses.some(openingHour => {
      if (openingHour.weekday === weekdayName) {
        weekdayIdToRemove = openingHour.id;
        return true; 
      }
      return false;
    });
  
    if (!weekdayIdToRemove) {
      console.log(`Es wurde kein Wochentag mit dem Namen ${weekdayName} gefunden.`);
      return;
    }

    fetch('http://localhost:8080/edit-attraction/', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        currentName: attraction.name,     
        openingHourIdsToRemove: [weekdayIdToRemove],
      })
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Fehler beim Speichern der Daten');
      }
      console.log('Daten erfolgreich gespeichert');
    })
    .catch(error => {
      console.error('Fehler beim Speichern der Daten:', error);
    });
}

//Tag soll hinzugefügt werden
handleAddTag = (attractionName) => {

  fetch('http://localhost:8080/edit-attraction/', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        currentName: attractionName,        
        filterTagsToAdd: ["neuerTag"]        
      })
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Fehler beim Speichern der Daten');
      }
      console.log('Daten erfolgreich gespeichert');
    })
    .catch(error => {
      console.error('Fehler beim Speichern der Daten:', error);
    });
}

  
  
  // Methode zur Aktualisierung der Attraktionsdaten über PUT-Anfrage
  updateAttractionData(attraction) {
    const openingHours = attraction.openingHoursResponses;

    var newOpeningHours= [];

    for (let i = 0; i < openingHours.length; i++) {
      newOpeningHours.push({weekday: openingHours[i].weekday,
        startTimeHour: this.splitTimeAttribute(openingHours[i].startTime).hours,
        startTimeMinute: this.splitTimeAttribute(openingHours[i].startTime).minutes,
        endTimeHour: this.splitTimeAttribute(openingHours[i].endTime).hours,
        endTimeMinute: this.splitTimeAttribute(openingHours[i].endTime).minutes,
    });
    }
    const {newImageUrl }= this.state;

    fetch('http://localhost:8080/edit-attraction/', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        currentName: attraction.name,
        newName: attraction.updatedName || attraction.name,
        newDescription: attraction.description,
        newImageUrl: newImageUrl,
        openingHoursToAdd: newOpeningHours,
        openingHourIdsToRemove: this.state.openingHourIdsToRemove, 
        filterTagsToAdd: attraction.filterTagResponses, 
        filterTagsToRemove: attraction.filterTagsToRemove
        
      })
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Fehler beim Speichern der Daten');
      }
      console.log('Daten erfolgreich gespeichert');
    })
    .catch(error => {
      console.error('Fehler beim Speichern der Daten:', error);
    });
  }
  

  //neuer wochentag
  handleAddNewWeekday = (index) => {
    const { attractions } = this.state;
    const attraction = attractions[index];
     
    fetch('http://localhost:8080/edit-attraction/', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        currentName: attraction.name,
        openingHoursToAdd: [{
          weekday: "neuer Wochentag",
          startTimeHour: "9",
          startTimeMinute: "0",
          endTimeHour: "17",
          endTimeMinute: "0"
        }]
      })
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Fehler beim Speichern der Daten');
      }
      console.log('Daten erfolgreich gespeichert');
      this.setState(prevState => ({
        attractions: [
          ...prevState.attractions,
          {
            ...attraction,
            openingHoursResponses: [
              ...attraction.openingHoursResponses,
              {
                weekday: "neuer Wochentag",
                startTime: "09:00:00",
                endTime: "17:00:00"
              }
            ]
          }
        ]
      }));
    })
    .catch(error => {
      console.error('Fehler beim Speichern der Daten:', error);
    });
  }

  //attraktion die gelöscht werden soll
  handleDeleteAttraction= () => {
    const { attractionToDelete } = this.state;

    fetch(`http://localhost:8080/delete-attraction/?attractionName=${attractionToDelete}`, {
        method: 'DELETE',
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Fehler beim Löschen der Attraktion');
        }
        console.log('Attraktion erfolgreich gelöscht');
      })
      .catch(error => {
        console.error('Fehler beim Löschen der Attraktion:', error);
      });
  }

  //Attraktion hinzufügen
  handleAddNewAttraction = () => {
    fetch('http://localhost:8080/create-attraction/', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    name: 'Attraktions Name',
    description: 'Beschreibung der Attraktion',
    openingHours: [
      {
        weekday: 'Wochentag',
        startTimeHour: 9,
        startTimeMinute: 0,
        endTimeHour: 17,
        endTimeMinute: 0
      }
    ],
    filterTags: [
      'Tag1',
      'Tag2'
    ]
  })
})
.then(response => {
  if (!response.ok) {
    throw new Error('Fehler beim Erstellen der Attraktion');
  }
  console.log('Attraktion erfolgreich erstellt');
})
.catch(error => {
  console.error('Fehler beim Erstellen der Attraktion:', error);
});
}

  render() {
    const attractionsToDisplay = this.state.selectedTags.length > 0 ? this.state.filteredAttractions : this.state.attractions;

    return (
      <div>
        <p style={{ position: 'absolute', top: '-350px', bottom: '5%', left: '20px', fontSize: '24px', color: '#FFFFFF' }}>Attraktionen</p>
        <div style={{ position: 'fixed', top: '35%', transform: 'translateY(-50%)', marginLeft: '25vw', marginBottom: "5vw", marginRight: '20vw', marginTop: '300px', maxHeight: '70vh', overflowY: 'auto' }}>
          {attractionsToDisplay.map((attraction, index) => (
            <div key={index} style={{ border: '2px solid #006400', marginBottom: '3%', display: 'flex', flexDirection: 'column',overflowX: 'auto', overflowY: 'auto', borderCollapse: 'collapse', width: '100%' }}>
              <div style={{ display: 'flex' }}>
                <div style={{ width: '300px', height: '200px', borderRight: '2px solid #006400', borderBottom: '2px solid #006400', padding: '10px' }}>
                  {/* Hier das Logo */}
                  <img src={attraction.imageUrl} alt={attraction.name} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                </div>
                <div style={{ flex: 1, padding: '10px', borderRight: '2px solid #006400' }}>
                  {/* Nicht bearbeitbares Textfeld für den aktuellen Namen */}
                  <p style={{ fontWeight: 'bold' }}>Alter Attraktions Name</p>
                  <input type="text" value={attraction.name} readOnly />
                  {/* Attraktionsname als bearbeitbares Textfeld */}
                  <p style={{ fontWeight: 'bold' }}>Neuer Attraktions Name</p>
                  <input type="text" value={attraction.updatedName} onChange={(e) => this.handleAttractionChange(index, 'updatedName', e.target.value)} />
                  {/* Beschreibung als bearbeitbares Textfeld */}
                  <p style={{ fontWeight: 'bold' }}>Beschreibung</p>
                  <textarea value={attraction.description} onChange={(e) => this.handleAttractionChange(index, 'description', e.target.value)} />
                </div>
                {/* Hier die Spalte für die Öffnungszeiten */}
                <div style={{ flex: 1, padding: '10px'}}>
                  <div>
                    <p style={{ fontWeight: 'bold', textDecoration: 'underline' }}>Öffnungszeiten</p>
                    {/* Öffnungszeiten als bearbeitbare Textfelder */}

                    {attraction.openingHoursResponses.map((openingHour, hourIndex) => (
                      <div key={hourIndex}>
                        <div style={{ flex: 1, padding: '10px'}}>
                        <input type="text" value={openingHour.weekday} onChange={(e) => this.handleOpeningHoursChange(index, hourIndex, 'weekday', e.target.value)} />
                        von
                        <input type="text" value={openingHour.startTime} onChange={(e) => this.handleOpeningHoursChange(index, hourIndex, 'startTime', e.target.value)} />
                        bis
                        <input type="text" value={openingHour.endTime} onChange={(e) => this.handleOpeningHoursChange(index, hourIndex, 'endTime', e.target.value)} />
                      </div>
                      </div>
                    ))}
                  </div>
                </div>
                {/* "Speichern"-Button*/}
                <div style={{ flex: '0 0 auto', padding: '10px' }}>
                  <button style={{backgroundColor: 'black' }} onClick={() => this.handleSaveClick(index)}>Speichern</button>
                  <div style={{ flex: '0 0 auto', padding: '10px' }}>
                <button style={{backgroundColor: 'black' }} onClick={() => this.handleAddNewWeekday(index)}>Neuer Wochentag</button>
                </div>
                <div style={{ flex: '0 0 auto', padding: '10px' }}>
                  <button style={{backgroundColor: 'black' }} onClick={() => this.handleRemoveWeekday(index, this.state.weekdayToRemove)}>Wochentag Löschen</button>
                  <div style={{ flex: '0 0 auto', padding: '10px' }}>
                      <input type="text" value={this.state.weekdayToRemove} onChange={(e) => this.setState({ weekdayToRemove: e.target.value })} placeholder="Wochentag Name" />
                      </div>
                      <div style={{ flex: '0 0 auto', padding: '10px' }}>
                      <button style={{backgroundColor: 'black' }} onClick={() => this.handleAddTag(attraction.name)}>Tag Hinzufügen</button>
                  </div>
                  <div style={{ flex: '0 0 auto', padding: '10px' }}>
                      <input type="text" value={this.state.newImageUrl} onChange={(e) => this.setState({index, newImageUrl: e.target.value })} placeholder="Bild URl" />
                      </div>
                  </div>
                </div>
              </div>
              <div style={{ display: 'flex', justifyContent: 'center', borderTop: '2px solid #006400', padding: '10px' }}>
                {/* Hier die bearbeitbaren Tags */}
                {attraction.filterTagResponses.map((tag, tagIndex) => (
                  <div key={tagIndex} style={{ marginRight: '5px' }}>
                    {/* Bearbeitbares Textfeld für den Tag */}
                    <input type="text" value={tag} onChange={(e) => this.handleTagChange(index, tagIndex, e.target.value)} />
                    </div>
                ))}
              </div>
            </div>
          ))}
        </div>
        <div style={{ position: 'fixed', top: '15%', left: '25%', backgroundColor: '#006400', padding: '10px', border: '1px solid #ddd', borderRadius: '5px' }}>
          <p style={{ fontWeight: 'bold' }}>Alle Tags</p>
          {this.state.allTags.map((tag, tagIndex) => (
            <button key={tagIndex} style={{ marginRight: '5px', backgroundColor: this.state.selectedTags.includes(tag) ? 'blue' : 'black' }} onClick={() => this.handleTagButtonClick(tag)}>
            {tag}
          </button>
        ))}
      </div>
      <div style={{ padding: '10px' }}>
            <button style={{backgroundColor: 'black' }} onClick={this.handleAddNewAttraction}>Neue Attraktion hinzufügen</button>
          </div>
          <div style={{ padding: '10px' }}>
            <button style={{backgroundColor: 'black' }} onClick={this.handleDeleteAttraction}>Attraktion Loeschen</button>
            <div style={{ flex: '0 0 auto', padding: '10px' }}>
              <input type="text" value={this.state.attractionToDelete} onChange={(e) => {e.stopPropagation(); this.setState({ attractionToDelete: e.target.value });
              }}  placeholder="Zu löschende Attraktion" />
            </div>
          </div>
    </div>
    
  );
}
}

export default AttractionsPage;