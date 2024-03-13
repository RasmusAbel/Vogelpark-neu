import React from 'react';
import HomePage from './Pages/HomePage';
import AttractionsPage from './Pages/AttractionsPage';
import ToursPage from './Pages/ToursPage';

// Platzhalter für das Bild
const backgroundImageUrl = 'placeholder_image.jpg';

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      currentPage: null,
      textFields: {
        Name: '',
        Adresse: '',
        Beschreibung: ''
      }
    };
  }

  componentDidMount() {
    // Hier senden Sie eine AJAX-Anfrage, um Daten von der REST-API abzurufen
    fetch('http://localhost:8080/bird-park-basic-info/')
      .then(response => response.json())
      .then(data => {
        // Extrahieren der relevanten Daten und Aktualisieren des Zustands
        const { name, address, description } = data;
        this.setState({
          textFields: {
            ...this.state.textFields,
            Name: name,
            Adresse: address,
            Beschreibung: description
          }
        });

        // Ausgabe des aktualisierten Zustands in die Konsole
        console.log(this.state.textFields);
      })
      .catch(error => {
        console.error('Fehler beim Abrufen der Daten:', error);
      });
  }

  gotoPage = (page) => {
    this.setState({ currentPage: page });
  }

  render() {
    const { currentPage, textFields } = this.state;
    return (
      <div style={{ position: 'relative' }}>
        {/* Bild als Hintergrund für die obere Leiste */}
        <div style={{ 
          backgroundImage: `url(https://www.restaurant-vogelpark-berghausen.de/uploads/w2hlKvG7/767x0_2000x0/voegel-hintergrund.png)`, 
          backgroundSize: 'cover', 
          position: 'fixed', top: 0, left: 0, right: 0,
          backgroundColor: '#000000',
          height: '100px', 
          width: '100%', 
          display: 'flex', 
          alignItems: 'center', 
          padding: '0 20px' 
        }}>
          <button style={{ position: 'fixed', top: '25px', left: 'calc(50% + -300px)', width: '100px', height: '50px', cursor: 'pointer', marginLeft: '20px', color: '#ffffff', backgroundColor: '#1C7F00', border: 'none', borderRadius: '5px', padding: '5px 10px' }} onClick={() => this.gotoPage(null)}>Home</button>
          <button style={{ position: 'fixed', top: '25px', left: '50%', transform: 'translateX(-50%)', height: '50px', cursor: 'pointer', marginLeft: '20px', color: '#ffffff', backgroundColor: '#1C7F00', border: 'none', borderRadius: '5px', padding: '5px 10px' }} onClick={() => this.gotoPage('Attraktionen')}>Attraktionen</button>
          <button style={{ position: 'fixed', top: '25px', left: 'calc(50% + 200px)', width: '100px', height: '50px', cursor: 'pointer', marginLeft: '20px', color: '#ffffff', backgroundColor: '#1C7F00', border: 'none', borderRadius: '5px', padding: '5px 10px' }} onClick={() => this.gotoPage('Touren')}>Touren</button>
        </div>

        {/* Seiteninhalte basierend auf dem aktuellen Seitenzustand anzeigen */}
        {currentPage === null && <HomePage textFields={textFields} />}
        {currentPage === 'Attraktionen' && <AttractionsPage />}
        {currentPage === 'Touren' && <ToursPage />}
      </div>
    );
  }
}

export default App;
