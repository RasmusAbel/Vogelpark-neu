import React from 'react';

class ToursPage extends React.Component {
  state = {
    tours: [], // Hier werden die Tourdaten gespeichert
    allAttractions: [], // Hier werden alle verfügbaren Attraktionen gespeichert
    selectedAttractions: [], // Hier werden die ausgewählten Attraktionen gespeichert
    filteredTours: [], // Hier werden die gefilterten Touren gespeichert
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

  render() {
    const toursToDisplay = this.state.selectedAttractions.length > 0 ? this.state.filteredTours : this.state.tours;

    return (
      <div>
        <p style={{ position: 'absolute', top: '-350px', left: '20px', fontSize: '24px', color: '#FFFFFF' }}>Touren</p>
        <div style={{ position: 'fixed', top: '35%', transform: 'translateY(-50%)', marginLeft: '25vw', marginRight: '20vw', marginTop: '300px', maxHeight: '70vh', overflowY: 'auto' }}>
          {toursToDisplay.map((tour, index) => (
            <div key={index} style={{ border: '2px solid #006400', marginBottom: '20px', display: 'flex',  flexDirection: 'column' }}>
              <div style={{ display: 'flex' }}>
              <div style={{ width: '300px', height: '200px', borderRight: '2px solid #006400', borderBottom: '2px solid #006400', padding: '10px' }}>
                  {/* Hier das Bild */}
                  <img src={tour.imageUrl} alt={tour.name} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                </div>

                <div style={{ display: 'flex' }}>
                <div style={{ width: '200px', height: '200px', borderRight: '2px solid #006400', borderBottom: '2px solid #006400', padding: '10px' }}>
                    {/* Hier der Tourname */}
                    <p style={{ fontWeight: 'bold', textDecoration: 'underline' }}>{tour.name}</p>
                    {/* Hier die Beschreibung */}
                    <p>{tour.description}</p>
                    
                  </div>
                </div>

                <div style={{ flex: 1, padding: '10px' }}>
                  {/* Hier die Zeit */}
                  <div>
                    <p style={{ fontWeight: 'bold', textDecoration: 'underline' }}>Zeit</p>
                    <p>{tour.startTime} - {tour.endTime}</p>
                     {/* Hier die Dauer */}
                    <p style={{ fontWeight: 'bold', textDecoration: 'underline' }}>Dauer</p>
                    <p>{tour.duration}</p>
                  </div>
                </div>
              </div>

              {/* Hier die Attraktionen */}
              <div style={{ display: 'flex', justifyContent: 'center', borderTop: '2px solid #006400', padding: '10px' }}>
                {tour.attractionNames.map((attraction, attractionIndex) => (
                  <div style={{ width: "200px", justifyContent: 'center',}}>
                    <button key={attractionIndex} style={{ marginRight: '5px', backgroundColor: this.state.selectedAttractions.includes(attraction) ? 'blue' : 'black' }} onClick={() => this.handleAttractionButtonClick(attraction)}>{attraction}</button>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
        <div style={{ position: 'fixed', top: '15%', left: '25%', backgroundColor: '#006400', padding: '10px', border: '1px solid #ddd', borderRadius: '5px' }}>
          <p style={{ fontWeight: 'bold' }}>Alle Attraktionen</p>
          {this.state.allAttractions.map((attraction, attractionIndex) => (
            <button key={attractionIndex} style={{ marginRight: '5px', backgroundColor: this.state.selectedAttractions.includes(attraction) ? 'blue' : 'black' }} onClick={() => this.handleAttractionButtonClick(attraction)}>{attraction}</button>
          ))}
        </div>
      </div>
    );
  }

}

export default ToursPage;
