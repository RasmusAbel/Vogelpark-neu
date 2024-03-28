import React from 'react';

class AttractionsPage extends React.Component {
  state = {
    attractions: [], // Hier werden die Attraktionsdaten gespeichert
    allTags: [], // Hier werden alle verfügbaren Tags gespeichert
    selectedTags: [], // Hier werden die ausgewählten Tags gespeichert
    filteredAttractions: [], // Hier werden die gefilterten Attraktionen gespeichert
  };

  //Daten aus der Datenbank holen
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

  //Tag wurde geklickt für das sortieren
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
                  <img src={attraction.imageUrl} alt={attraction.name} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
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
                <div style={{ flex: 1, padding: '10px', borderLeft: '2px solid #006400'}}>
                  <div>
                    <p style={{ fontWeight: 'bold', textDecoration: 'underline' }}>Touren</p>
                    {attraction.tourNames.map((tourName, index) => (
                      <p key={index} style={{ marginBottom: '2px' }}>{tourName}</p>
                    ))}
                  </div>
                </div>
              </div>
              <div style={{ display: 'flex', justifyContent: 'center', borderTop: '2px solid #006400', padding: '10px' }}>
                {/* Hier die Tags als Buttons */}
                {attraction.filterTagResponses.map((tag, tagIndex) => (
                  <button key={tagIndex} style={{ marginRight: '5px', backgroundColor: this.state.selectedTags.includes(tag) ? 'blue' : 'black' }} onClick={() => this.handleTagButtonClick(tag)}>{tag}</button>
                ))}
              </div>
            </div>
          ))}
        </div>
        <div style={{ position: 'fixed', top: '15%', left: '25%', backgroundColor: '#006400', padding: '10px', border: '1px solid #ddd', borderRadius: '5px' }}>
          <p style={{ fontWeight: 'bold' }}>Alle Tags</p>
          {this.state.allTags.map((tag, tagIndex) => (
            <button key={tagIndex} style={{ marginRight: '5px', backgroundColor: this.state.selectedTags.includes(tag) ? 'blue' : 'black' }} onClick={() => this.handleTagButtonClick(tag)}>{tag}</button>
          ))}
        </div>
      </div>
    );
  }
}

export default AttractionsPage;

