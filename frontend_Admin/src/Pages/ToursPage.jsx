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
    tourToDelete: '', //Tour die gelöscht werden soll
    newImageUrl: '', //Neue Bild Url
    openingHourIdsToRemove: [], //OpeningHours die gelöscht werden sollen
  };

  //Daten aus der Datenbank
  componentDidMount() {
    fetch('http://localhost:8080/all-tours/')
      .then(response => response.json())
      .then(data => {
        this.setState({ tours: data });
        const allAttractions = [...new Set(data.flatMap(tour => tour.attractionNames))];
        this.setState({ allAttractions });
      })
      .catch(error => {
        console.error('Fehler beim Abrufen der Daten:', error);
      });
  }

  //Button wird gedrückt zum sortieren der Attraktionen
  handleAttractionButtonClick = (attraction) => {
    if (this.state.selectedAttractions.includes(attraction)) {
      this.setState(prevState => ({
        selectedAttractions: prevState.selectedAttractions.filter(selectedAttraction => selectedAttraction !== attraction)
      }), this.filterTours);
    } else {
      this.setState(prevState => ({
        selectedAttractions: [...prevState.selectedAttractions, attraction]
      }), this.filterTours);
    }
  }

  //Touren werden nach attraktionen die ausgewählt sind gefiltert
  filterTours = () => {
    if (this.state.selectedAttractions.length > 0) {
      const attractionParams = this.state.selectedAttractions.map(attraction => `attractionName=${encodeURIComponent(attraction)}`).join('&');
      const url = `http://localhost:8080/tours-by-attractions/?${attractionParams}`;
      fetch(url)
        .then(response => response.json())
        .then(data => {
          this.setState({ filteredTours: data });
        })
        .catch(error => {
          console.error('Fehler beim Abrufen der gefilterten Touren:', error);
        });
    } else {
      this.setState({ filteredTours: this.state.tours });
    }
  }

  //Tour Daten werden bearbeitet
  handleTourChange(index, field, value) {
    const tours = [...this.state.tours];
    tours[index][field] = value;
    this.setState({ tours });
  }

  //Dauer wird bearbeitet
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
    return {tours};
  });
  }

  //Preis wird bearbeitet
  handlePriceChange(index, field, value){
    const tours = [...this.state.tours];
    tours[index][field] = value;
    this.setState({ tours });
  }

  //Methode um die Zeiten in Minuten und Stunden aufzuteilen
  splitTimeAttribute(timeAttribute) {
    const [hours, minutes] = timeAttribute.split(':').map(part => parseInt(part));
    return { hours, minutes };
}

  //Speichern Button wird gedrückt
  handleSaveClick(index){
    const { tours } = this.state;
    const tour = tours[index];

    if (!tour.filterAttractionsToRemove) {
      tour.filterAttractionsToRemove = [""];
    }

    tour.attractionNames = tour.attractionNames.filter(attraction => attraction !== "");

    const currentName = tour.name;
    const newName = tour.updatedName || tour.name; 
    const newDescription = tour.description;
    const newPrice = tour.price;
    const { newImageUrl } = this.state;  
    const newStartTime = tour.startTime;
    const newEndTime = tour.endTime;
    const attractions = tour.attractionNames;
    const filterAttractionsToAdd = tour.attractionNames; 
    const filterAttractionsToRemove = tour.filterAttractionsToRemove;

this.setState(prevState => ({
  currentName,
  newName,
  newDescription,
  newPrice,
  newStartTime,
  newEndTime,
  filterAttractionsToAdd,
  filterAttractionsToRemove: (tour.filterAttractionsToRemove && tour.filterAttractionsToRemove.length > 0) ? tour.filterAttractionsToRemove : [""], 
  }), () => {
  this.updateTourData(tour);
  window.location.href = 'http://localhost:8082'
});    
  }

  //Attraktionen werden bearbeitet
  handleAttractionChange (tourIndex, AttractionIndex, newValue) {
    this.setState(prevState => {
      const tours = [...prevState.tours];
      const tour = { ...tours[tourIndex] };
      const updatedAttractions = [...tour.attractionNames];
      let filterAttractionsToAdd = tour.filterAttractionsToAdd ? [...tour.filterAttractionsToAdd] : [];
      let filterAttractionsToRemove = tour.filterAttractionsToRemove ? [...tour.filterAttractionsToRemove] : [];
  
      updatedAttractions[AttractionIndex] = newValue;

      if (!filterAttractionsToRemove.includes(tour.attractionNames[AttractionIndex]) && newValue !== tour.attractionNames[AttractionIndex]) {
        filterAttractionsToRemove.push(tour.attractionNames[AttractionIndex]);
        filterAttractionsToAdd.push(newValue);
      } else if (filterAttractionsToRemove.includes(tour.attractionNames[AttractionIndex]) && newValue === tour.attractionNames[AttractionIndex]) {
        const indexToRemove = filterAttractionsToRemove.indexOf(tour.attractionNames[AttractionIndex]);
        filterAttractionsToRemove.splice(indexToRemove, 1);
        const indexToAdd = filterAttractionsToAdd.indexOf(newValue);
        filterAttractionsToAdd.splice(indexToAdd, 1);
      }

      filterAttractionsToRemove = filterAttractionsToRemove.filter(attraction => !filterAttractionsToAdd.includes(attraction));

      tour.attractionNames = updatedAttractions;
      tour.filterAttractionsToAdd = filterAttractionsToAdd;
      tour.filterAttractionsToRemove = filterAttractionsToRemove;
      tours[tourIndex] = tour;
      return { tours };
    });
  }

  //Attraktine wird hinzugefügt
  handleAddAttraction = (index) => {
    const { tours, attractionToAdd } = this.state;
    const tour = tours[index];
    
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
      })
      .catch(error => {
        console.error('Fehler beim Speichern der Daten:', error);
      });
  }

  //Tour wird gelöscht
  handleDeleteTour = () => {
    const { tourToDelete } = this.state;
    
    fetch(`http://localhost:8080/delete-tour/?tourName=${tourToDelete}`, {
        method: 'DELETE',
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Fehler beim Löschen der Tour');
        }
        console.log('Tour erfolgreich gelöscht');
      })
      .catch(error => {
        console.error('Fehler beim Löschen der Tour:', error);
      });
  }

  //Neu Tour wird erstellt
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

  //Tour Daten werden aktualisiert
  updateTourData(tour) {

var { hours, minutes } = this.splitTimeAttribute(tour.startTime);
const newStartTimeHour = hours;
const newStartTimeMinute = minutes;

var { hours, minutes } = this.splitTimeAttribute(tour.endTime);
const newEndTimeHour = hours;
const newEndTimeMinute = minutes;
const {newImageUrl }= this.state;

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
})
.catch(error => {
  console.error('Fehler beim Speichern der Daten:', error);
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
