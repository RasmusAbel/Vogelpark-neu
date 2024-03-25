import React from 'react';

class AttractionsPage extends React.Component {
  state = {
    attractions: [], // Hier werden die Attraktionsdaten gespeichert
    allTags: [], // Hier werden alle verfügbaren Tags gespeichert
    selectedTags: [], // Hier werden die ausgewählten Tags gespeichert
    filteredAttractions: [], // Hier werden die gefilterten Attraktionen gespeichert
  };

  componentDidMount() {
    // Hier senden Sie eine AJAX-Anfrage, um Daten von der REST-API abzurufen
    fetch('http://localhost:8080/all-attractions/')
      .then(response => response.json())
      .then(data => {
        // Aktualisieren Sie den Zustand mit den empfangenen Attraktionsdaten
        this.setState({ attractions: data });

        // Extrahieren Sie alle verfügbaren Tags
        const allTags = [...new Set(data.flatMap(attraction => attraction.filterTagResponses))];
        this.setState({ allTags });
      })
      .catch(error => {
        console.error('Fehler beim Abrufen der Daten:', error);
      });
  }

  handleTagButtonClick = (tag) => {
    // Überprüfen, ob der Tag bereits ausgewählt ist
    if (this.state.selectedTags.includes(tag)) {
      // Tag ist bereits ausgewählt, entfernen Sie ihn aus den ausgewählten Tags
      this.setState(prevState => ({
        selectedTags: prevState.selectedTags.filter(selectedTag => selectedTag !== tag)
      }), this.filterAttractions);
    } else {
      // Tag ist nicht ausgewählt, fügen Sie ihn zu den ausgewählten Tags hinzu
      this.setState(prevState => ({
        selectedTags: [...prevState.selectedTags, tag]
      }), this.filterAttractions);
    }
  }

  filterAttractions = () => {
    // Überprüfen Sie, ob Tags ausgewählt sind
    if (this.state.selectedTags.length > 0) {
        // Konstruieren Sie die URL basierend auf den ausgewählten Tags
        const tagParams = this.state.selectedTags.map(tag => `tag=${tag}`).join('&');
        const url = `http://localhost:8080/attractions-by-tags/?${tagParams}`;

        // Hier senden Sie eine AJAX-Anfrage, um die Attraktionen nach den ausgewählten Tags zu filtern
        fetch(url)
            .then(response => response.json())
            .then(data => {
                // Aktualisieren Sie den Zustand mit den gefilterten Attraktionen
                this.setState({ filteredAttractions: data });
            })
            .catch(error => {
                console.error('Fehler beim Abrufen der gefilterten Attraktionen:', error);
            });
    } else {
        // Keine Tags ausgewählt, verwenden Sie die vollständige Liste der Attraktionen
        this.setState({ filteredAttractions: this.state.attractions });
    }
}


  render() {
    const attractionsToDisplay = this.state.selectedTags.length > 0 ? this.state.filteredAttractions : this.state.attractions;

    return (
      <div>
        <p style={{ position: 'absolute', top: '-350px', left: '20px', fontSize: '24px', color: '#FFFFFF' }}>Attraktionen</p>
        <div style={{ position: 'fixed', top: '35%', transform: 'translateY(-50%)', marginLeft: '25vw', marginRight: '20vw', marginTop: '300px', maxHeight: '70vh', overflowY: 'auto' }}>
          {attractionsToDisplay.map((attraction, index) => (
            <div key={index} style={{ border: '2px solid #006400', marginBottom: '20px', display: 'flex', flexDirection: 'column' }}>
              <div style={{ display: 'flex' }}>
                <div style={{ width: '200px', height: '200px', borderRight: '2px solid #006400', borderBottom: '2px solid #006400', padding: '10px' }}>
                  {/* Hier das Logo */}
                  {/* Wenn ein Logo in den Daten vorhanden wäre, könnte es hier eingefügt werden */}
                </div>
                <div style={{ flex: 1, padding: '10px', borderRight: '2px solid #006400' }}>
                  {/* Hier der Attraktionsname */}
                  <p style={{ fontWeight: 'bold', textDecoration: 'underline' }}>{attraction.name}</p>
                  {/* Hier die Beschreibung */}
                  <p>{attraction.description}</p>
                </div>
                {/* Hier die Spalte für die Öffnungszeiten */}
                <div style={{ flex: 1, padding: '10px' }}>
                  <div>
                    <p style={{ fontWeight: 'bold', textDecoration: 'underline' }}>Öffnungszeiten</p>
                    {attraction.openingHoursResponses.map((openingHour, hourIndex) => (
                      <div key={hourIndex}>
                        <p>{openingHour.weekday} von {openingHour.startTime} bis {openingHour.endTime}</p>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
              <div style={{ display: 'flex', justifyContent: 'center', borderTop: '2px solid #006400', padding: '10px' }}>
                {/* Hier die Tags als Buttons */}
                {attraction.filterTagResponses.map((tag, tagIndex) => (
                  <button key={tagIndex} style={{ marginRight: '5px', backgroundColor: this.state.selectedTags.includes(tag) ? 'blue' : '' }} onClick={() => this.handleTagButtonClick(tag)}>{tag}</button>
                ))}
              </div>
            </div>
          ))}
        </div>
        <div style={{ position: 'fixed', top: '15%', left: '25%', backgroundColor: '#006400', padding: '10px', border: '1px solid #ddd', borderRadius: '5px' }}>
          <p style={{ fontWeight: 'bold' }}>Alle Tags</p>
          {this.state.allTags.map((tag, tagIndex) => (
            <button key={tagIndex} style={{ marginRight: '5px', backgroundColor: this.state.selectedTags.includes(tag) ? 'blue' : '' }} onClick={() => this.handleTagButtonClick(tag)}>{tag}</button>
          ))}
        </div>
      </div>
    );
  }
}

export default AttractionsPage;

