import React from 'react';

class ToursPage extends React.Component {
  state = {
    tours: [], // Hier werden die Tourdaten gespeichert
    allAttractions: [], // Hier werden alle verfügbaren Attraktionen gespeichert
    selectedAttractions: [], // Hier werden die ausgewählten Attraktionen gespeichert
    filteredTours: [], // Hier werden die gefilterten Touren gespeichert
    currentName: '', // Globaler Konstante für den aktuellen Namen
    newName: '', // Globaler Konstante für den neuen Namen
    newDescription: '', // Globaler Konstante für die neue Beschreibung
    newPriceCents: '', // Globaler Konstante für den neuen Preis
    newStartTimeHour: '', // Globaler Konstante für die Startzeit Stunde
    newStartTimeMinute: '', // Globaler Konstante für die Startzeit Minute
    newEndTimeHour: '', // Globaler Konstante für die Endzeit Stunde
    newEndTimeMinute: '', // Globaler Konstante für die Endzeit Minute
    attractionToAdd: '', // Globaler Konstante für den hinzuzufügenden Tag
    attractionToRemove: '', // Globaler Konstante für den zu entfernenden Tag
    tourToDelete: '',
    newImageUrl: '',
    openingHourIdsToRemove: [],
  };

  componentDidMount() {
    // Hier senden Sie eine AJAX-Anfrage, um Daten von der REST-API abzurufen
    fetch('http://localhost:8080/all-tours/')
      .then(response => response.json())
      .then(data => {
        // Aktualisieren Sie den Zustand mit den empfangenen Tourdaten
        this.setState({ tours: data });

        // Extrahieren Sie alle verfügbaren Attraktionen
        const allAttractions = [...new Set(data.flatMap(tour => tour.attractionNames))];
        this.setState({ allAttractions });
      })
      .catch(error => {
        console.error('Fehler beim Abrufen der Daten:', error);
      });
  }

  handleAttractionButtonClick = (attraction) => {
    // Überprüfen, ob die Attraktion bereits ausgewählt ist
    if (this.state.selectedAttractions.includes(attraction)) {
      // Attraktion ist bereits ausgewählt, entfernen Sie sie aus den ausgewählten Attraktionen
      this.setState(prevState => ({
        selectedAttractions: prevState.selectedAttractions.filter(selectedAttraction => selectedAttraction !== attraction)
      }), this.filterTours);
    } else {
      // Attraktion ist nicht ausgewählt, fügen Sie sie zu den ausgewählten Attraktionen hinzu
      this.setState(prevState => ({
        selectedAttractions: [...prevState.selectedAttractions, attraction]
      }), this.filterTours);
    }
  }

  filterTours = () => {
    // Überprüfen, ob Attraktionen ausgewählt sind
    if (this.state.selectedAttractions.length > 0) {
      // Konstruieren Sie die URL basierend auf den ausgewählten Attraktionen
      const attractionParams = this.state.selectedAttractions.map(attraction => `attractionName=${encodeURIComponent(attraction)}`).join('&');
      const url = `http://localhost:8080/tours-by-attractions?${attractionParams}`;

      // Hier senden Sie eine AJAX-Anfrage, um die Touren nach den ausgewählten Attraktionen zu filtern
      fetch(url)
        .then(response => response.json())
        .then(data => {
          // Aktualisieren Sie den Zustand mit den gefilterten Touren
          this.setState({ filteredTours: data });
        })
        .catch(error => {
          console.error('Fehler beim Abrufen der gefilterten Touren:', error);
        });
    } else {
      // Keine Attraktionen ausgewählt, verwenden Sie die vollständige Liste der Touren
      this.setState({ filteredTours: this.state.tours });
    }
  }

  handleTourChange(index, field, value) {
    const tours = [...this.state.tours];
    tours[index][field] = value;
    this.setState({ tours });
  }

  handleDurationChange(tourIndex, field, value){
    this.setState(prevState => {
      const tours = [...prevState.tours];
      const tour = { ...tours[tourIndex] };
      tour[field] = value; 
      if (field === 'startTime' || field === 'endTime') {
        const { hours, minutes } = this.splitTimeAttribute(value);
        tours[field === 'startTime' ? 'startTimeHour' : 'endTimeHour'] = hours;
        tours[field === 'startTime' ? 'startTimeMinute' : 'endTimeMinute'] = minutes;
    }
    tours[tourIndex] = tour;
    console.log(tours);
    return {tours};
  });
  }

  handlePriceChange(index, field, value){
    const tours = [...this.state.tours];
    tours[index][field] = value;
    this.setState({ tours });
  }

  splitTimeAttribute(timeAttribute) {
    const [hours, minutes] = timeAttribute.split(':').map(part => parseInt(part));
    console.log("Minute:", minutes, "hours", hours);
    return { hours, minutes };
}

  handleSaveClick(index){
    const { tours } = this.state;
    const tour = tours[index];

    if (!tour.filterAttractionsToRemove) {
      tour.filterAttractionsToRemove = [""];
    }

    tour.attractionNames = tour.attractionNames.filter(attraction => attraction !== "");
    // Current Name aus dem nicht bearbeitbaren Textfeld extrahieren
    const currentName = tour.name;
  
    // New Name aus dem bearbeitbaren Textfeld ziehen
    const newName = tour.updatedName || tour.name;
  
    // Neue Werte aus dem State lesen
    const newDescription = tour.description;

    const newPrice = tour.price;

    const { newImageUrl } = this.state;
    
    console.log("bildUrl", newImageUrl)

   
    
    const newStartTime = tour.startTime;
    //const newStartTimeMinute = minutes;

    const newEndTime = tour.endTime;
    //const newStartTimeMinute = minutes;

    
    // Überprüfen, ob neue Zeitdaten vorhanden sind, sonst die vorhandenen Daten verwenden
   
  
    const attractions = tour.attractionNames;
    const filterAttractionsToAdd = tour.attractionNames; // Hier setzen wir filterTagsToAdd auf die gewünschten Daten
  
    const filterAttractionsToRemove = tour.filterAttractionsToRemove;
   
    console.log("AttractiontoAdd", filterAttractionsToAdd);
    console.log("Attractionssvariable", tour.attractionNames);

// Update der Attraktion mit den aktuellen Daten
this.setState(prevState => ({
  currentName,
  newName,
  newDescription,
  newPrice,
  newStartTime,
  newEndTime,
 //newStartTimeHour, // Alle Öffnungszeiten hinzufügen
  //newStartTimeMinute,
  //newEndTimeHour,
  //newEndTimeMinute,
  filterAttractionsToAdd,
  filterAttractionsToRemove: (tour.filterAttractionsToRemove && tour.filterAttractionsToRemove.length > 0) ? tour.filterAttractionsToRemove : [""], // Setzen Sie filterAttractionsToRemove auf ein Array mit einem leeren String, wenn es leer oder nicht definiert ist
  }), () => {
  // Aufrufen der Methode zur Aktualisierung der Attraktionsdaten
  this.updateTourData(tour);
 // window.location.href = 'http://localhost:8082'
});


  
    // Aktualisierte Daten der Attraktion in der Konsole ausgeben
    console.log("Aktuelle Daten der Attraktion:");
    console.log("Current Name:", currentName);
    console.log("New Name:", newName);
    console.log("New Description:", newDescription);
    console.log("Tags:", attractions);
    console.log("Tags hinzuzufügen:", filterAttractionsToAdd);
    console.log("Tags zu entfernen:", filterAttractionsToRemove);
    
  }

  handleAttractionChange (tourIndex, AttractionIndex, newValue) {
    this.setState(prevState => {
      const tours = [...prevState.tours];
      const tour = { ...tours[tourIndex] };
      const updatedAttractions = [...tour.attractionNames];
      let filterAttractionsToAdd = tour.filterAttractionsToAdd ? [...tour.filterAttractionsToAdd] : [];
      let filterAttractionsToRemove = tour.filterAttractionsToRemove ? [...tour.filterAttractionsToRemove] : [];
  
      // Änderung des bearbeitbaren Tags
      updatedAttractions[AttractionIndex] = newValue;
      console.log("toadd", filterAttractionsToAdd);
      console.log("toremove", filterAttractionsToRemove);
      // Überprüfen, ob der Tag geändert wurde und ihn den entsprechenden Listen hinzufügen oder entfernen
      if (!filterAttractionsToRemove.includes(tour.attractionNames[AttractionIndex]) && newValue !== tour.attractionNames[AttractionIndex]) {
        filterAttractionsToRemove.push(tour.attractionNames[AttractionIndex]);
        filterAttractionsToAdd.push(newValue);
      } else if (filterAttractionsToRemove.includes(tour.attractionNames[AttractionIndex]) && newValue === tour.attractionNames[AttractionIndex]) {
        const indexToRemove = filterAttractionsToRemove.indexOf(tour.attractionNames[AttractionIndex]);
        filterAttractionsToRemove.splice(indexToRemove, 1);
        const indexToAdd = filterAttractionsToAdd.indexOf(newValue);
        filterAttractionsToAdd.splice(indexToAdd, 1);
      }
      
      // Entfernen von Duplikaten in filterAttractionsToRemove, die auch in filterattractionsToAdd vorhanden sind
      filterAttractionsToRemove = filterAttractionsToRemove.filter(attraction => !filterAttractionsToAdd.includes(attraction));

      tour.attractionNames = updatedAttractions;
      tour.filterAttractionsToAdd = filterAttractionsToAdd;
      tour.filterAttractionsToRemove = filterAttractionsToRemove;
      tours[tourIndex] = tour;
      console.log("Toure", tours)
      return { tours };
    });
  }

  handleAddAttraction = (index) => {
    const { tours, attractionToAdd } = this.state;
    const tour = tours[index];
    // Fügen Sie hier den neuen Tag zu den Daten hinzu
    // Verwenden Sie dazu den Wert von 'tagToAdd'
  
    // Beispielhaft: Aktualisieren Sie den Zustand mit dem neuen Tag
    console.log('Attraktionsname:', attractionToAdd);
    console.log('TourName:', tour.name);
    
    fetch('http://localhost:8080/edit-tour/', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          currentName: tour.name,        
          attractionNamesToAdd: [attractionToAdd]   
        })
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Fehler beim Speichern der Daten');
        }
        console.log('Daten erfolgreich gespeichert');
        // Hier können Sie entsprechende Aktionen ausführen, nachdem die Daten erfolgreich gespeichert wurden
      })
      .catch(error => {
        console.error('Fehler beim Speichern der Daten:', error);
        // Hier können Sie entsprechende Fehlerbehandlung durchführen
      });
  }

  handleDeleteTour = () => {
    const { tourToDelete } = this.state;
    
    console.log('TourName:', tourToDelete);
    
    fetch(`http://localhost:8080/delete-tour/${tourToDelete}`, {
        method: 'DELETE',
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Fehler beim Löschen der Tour');
        }
        console.log('Tour erfolgreich gelöscht');
        // Hier können Sie entsprechende Aktionen ausführen, nachdem die Tour erfolgreich gelöscht wurde
      })
      .catch(error => {
        console.error('Fehler beim Löschen der Tour:', error);
        // Hier können Sie entsprechende Fehlerbehandlung durchführen
      });
  }

  handleAddNewTour = () => {
    fetch('http://localhost:8080/create-tour/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: 'Tour Name',
        description: 'Beschreibung der Tour',
        priceCents: 500,
        startTimeHour: 11,
        startTimeMinute: 0,
        endTimeHour: 22,
        endTimeMinute: 0,
        attractionNames: [ ]
      })
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Fehler beim Erstellen der Tour');
      }
      console.log('Tour erfolgreich erstellt');
    })
    .catch(error => {
      console.error('Fehler beim Erstellen der Tour:', error);
    });
  }

  updateTourData(tour) {
// PUT-Anfrage an die REST-API senden
console.log("Tags die hinzugefügt werden:",  tour.attractionNames); // Hier verwenden wir attraction.filterTagResponses
console.log("Tags die entfernt werden:",  tour.filterAttractionsToRemove);
console.log("die tour", tour)
var { hours, minutes } = this.splitTimeAttribute(tour.startTime);
const newStartTimeHour = hours;
const newStartTimeMinute = minutes;

var { hours, minutes } = this.splitTimeAttribute(tour.endTime);
const newEndTimeHour = hours;
const newEndTimeMinute = minutes;
const {newImageUrl }= this.state;
console.log("die Url", newImageUrl);
console.log("startZeit stunde", newStartTimeHour)
fetch('http://localhost:8080/edit-tour/', {
  method: 'PUT',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
  currentName: tour.name,
  newName: tour.updatedName || tour.name,
  newDescription:tour.description,
  newPriceCents: tour.price,
  newImageUrl: newImageUrl,
  newStartTimeHour: newStartTimeHour,
  newStartTimeMinute:  newStartTimeMinute,
  newEndTimeHour:  newEndTimeHour,
  newEndTimeMinute:  newEndTimeMinute,
  attractionNamesToAdd: tour.attractionNames,
  attractionNamesToRemove: tour.filterAttractionsToRemove
    
  })
})
.then(response => {
  if (!response.ok) {
    throw new Error('Fehler beim Speichern der Daten');
  }
  console.log('Daten erfolgreich gespeichert');
  // Hier können Sie entsprechende Aktionen ausführen, nachdem die Daten erfolgreich gespeichert wurden
})
.catch(error => {
  console.error('Fehler beim Speichern der Daten:', error);
  // Hier können Sie entsprechende Fehlerbehandlung durchführen
});
  }

  render() {
    const toursToDisplay = this.state.selectedAttractions.length > 0 ? this.state.filteredTours : this.state.tours;

    return (
      <div>
        <p style={{ position: 'absolute', top: '-350px',bottom: "10vw", left: '20px', fontSize: '24px', color: '#FFFFFF' }}>Touren</p>
        <div style={{ position: 'fixed', top: '35%', transform: 'translateY(-50%)', marginLeft: '25vw', marginRight: '20vw', marginTop: '300px', maxHeight: '70vh', overflowY: 'auto' }}>
          {toursToDisplay.map((tour, index) => (
            <div key={index} style={{ border: '2px solid #006400',bottom:"3%", marginBottom: '5%', display: 'flex',  flexDirection: 'column' }}>
              <div style={{ display: 'flex' }}>
              <div style={{ width: '300px', height: '200px', borderRight: '2px solid #006400', borderBottom: '2px solid #006400', padding: '10px' }}>
                  {/* Hier das Bild */}
                  <img src={tour.imageUrl} alt={tour.name} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                </div>
                <div style={{ display: 'flex' }}>
                <div style={{flex: 1, width: '300px',borderRight: '2px solid #006400', borderBottom: '2px solid #006400', padding: '10px' }}>
                    {/* Hier der Tourname */}
                    <p style={{ marginBottom: '3px', fontWeight: 'bold' }}>Alter Tour Name</p>
                    <input type="text" value={tour.name} readOnly />
                    <p style={{marginTop: '6px', marginBottom: '5px', fontWeight: 'bold' }}>Neuer Tour Name</p>
                    <input type="text"  value={tour.updatedName !== tour.name ? tour.updatedName : tour.name} onChange={(e) => this.handleTourChange(index, 'updatedName', e.target.value)} />
                    {/* Hier die Beschreibung */}
                    <p style={{ marginBottom: '5px', fontWeight: 'bold' }}>Beschreibung</p>
                    <textarea value={tour.description} onChange={(e) => this.handleTourChange(index, 'description', e.target.value)} />
                    <p style={{ fontWeight: 'bold', textDecoration: 'underline' }}>Preis</p>
                    <input type="text" value={tour.price} onChange={(e) => this.handlePriceChange(index, 'price', e.target.value)} />
                  </div>
                </div>

                <div style={{ flex: 1, padding: '10px' }}>
                  {/* Hier die Zeit */}
                  <div>
                    <p style={{ fontWeight: 'bold', textDecoration: 'underline' }}>Zeit</p>
                    <input type="text" value={tour.startTime} onChange={(e) => this.handleDurationChange(index, 'startTime', e.target.value)} />
                    -
                    <input type="text" value={tour.endTime} onChange={(e) => this.handleDurationChange(index, 'endTime', e.target.value)} />
                     {/* Hier die Dauer */}
                    <p style={{ fontWeight: 'bold', textDecoration: 'underline' }}>Dauer</p>
                    <p>{tour.duration}</p>
                  </div>
                </div>
                <div style={{ flex: '0 0 auto', padding: '10px' }}>
                  <button style={{backgroundColor: 'black' }} onClick={() => this.handleSaveClick(index)}>Speichern</button>
                  <div style={{ flex: '0 0 auto', padding: '10px' }}>
                      <button style={{backgroundColor: 'black' }} onClick={() => this.handleAddAttraction(index, tour.name)}>Attraktion Hinzufügen</button>
                      <div style={{ flex: '0 0 auto', padding: '10px' }}>
                      <input type="text" value={this.state.attractionToAdd} onChange={(e) => this.setState({index, attractionToAdd: e.target.value })} placeholder="Attraktion Name" />
                      </div>
                   </div>
                     <div style={{ flex: '0 0 auto', padding: '10px' }}>
                      <input type="text" value={this.state.newImageUrl} onChange={(e) => this.setState({index, newImageUrl: e.target.value })} placeholder="Bild URl" />
                      </div>
                   </div>
              
              </div>
              {/* Hier die Attraktionen */}
              <div style={{ display: 'flex', justifyContent: 'center', borderTop: '2px solid #006400', padding: '10px' }}>
                {tour.attractionNames.map((attraction, attractionIndex) => (
                  <div key={attractionIndex} style={{ marginRight: '5px' }}>
                  {/* Bearbeitbares Textfeld für den Tag */}
                  <input type="text" value={attraction} onChange={(e) => this.handleAttractionChange(index, attractionIndex, e.target.value)} />
                  {/* Nicht bearbeitbares Textfeld für den Tag */}
                </div>
                ))}
              </div>
            </div>
          ))}
        </div>
        <div style={{ position: 'fixed', top: '15%', left: '25%', backgroundColor: '#006400', padding: '10px', border: '1px solid #ddd', borderRadius: '5px' }}>
          <p style={{ fontWeight: 'bold' }}>Alle Attraktionen</p>
          {this.state.allAttractions.map((attraction, attractionIndex) => (
            <button style={{ marginRight: '5px', backgroundColor: this.state.selectedAttractions.includes(attraction) ? 'blue' : 'black' }} onClick={() => this.handleAttractionButtonClick(attraction)}>{attraction}</button>
          ))}
        </div>
        <div style={{ padding: '10px' }}>
            <button style={{backgroundColor: 'black' }} onClick={this.handleAddNewTour}>Neue Tour hinzufügen</button>
          </div>
          <div style={{ padding: '10px' }}>
            <button style={{backgroundColor: 'black' }} onClick={this.handleDeleteTour}>Tour Loeschen</button>
            <div style={{ flex: '0 0 auto', padding: '10px' }}>
              <input type="text" value={this.state.tourToDelete} onChange={(e) => {e.stopPropagation(); this.setState({ tourToDelete: e.target.value });
              }}  placeholder="Zu löschende Tour" />
            </div>
          </div>
      </div>
    );
  }

}

export default ToursPage;
