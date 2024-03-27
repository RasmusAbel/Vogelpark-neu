import React from 'react';
import HomePage from './Pages/HomePage';
import AttractionsPage from './Pages/AttractionsPage';
import ToursPage from './Pages/ToursPage';

document.body.style.backgroundColor = '#1a1a1a';


class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      currentPage: null,
      LogoUrl: '',
      textFields: {
        Name: '',
        Address: '',
        Description: '',
      },
      openingHours: [] // Hier wird ein leeres Array initialisiert
    };
  }

  componentDidMount() {
    fetch('http://localhost:8080/bird-park-basic-info/')
      .then(response => response.json())
      .then(data => {
        const { name, address, description, openingHoursResponses, logoUrl } = data;
        console.log('Erhaltene Daten:', data); // Konsolenausgabe der erhaltenen Daten
  
        // Speichern der Öffnungszeiten als Array im Zustand
        this.setState({
          LogoUrl: logoUrl,
          textFields: {
            ...this.state.textFields,
            Name: name,
            Address: address,
            Description: description,
          },
          openingHours: openingHoursResponses // Speichern des Arrays der Öffnungszeiten
        });
  
        console.log('Aktualisierter Zustand:', this.state); // Konsolenausgabe des aktualisierten Zustands
      })
      .catch(error => {
        console.error('Fehler beim Abrufen der Daten:', error);
      });
  }
  
  

  gotoPage = (page) => {
    this.setState({ currentPage: page });
  }

  render() {
    const { currentPage, textFields, openingHours, LogoUrl  } = this.state;
    return (
      <div style={{position: 'relative' }}>
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

        {currentPage === null && <HomePage textFields={textFields} openingHours={openingHours} LogoUrl={LogoUrl} />}
        {currentPage === 'Attraktionen' && <AttractionsPage />}
        {currentPage === 'Touren' && <ToursPage />}
      </div>
    );
  }
}

export default App;
