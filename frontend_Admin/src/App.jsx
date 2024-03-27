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
      textFields: {
        Name: '',
        Adresse: '',
        Beschreibung: '',
        LogoUrl: '',
        Wochentag: '',
        Oeffnungszeit: '',
        Schließzeit: ''
      },
      basicData: null // Fügen Sie basicData zum Zustand hinzu
    };
  }

  componentDidMount() {
    this.fetchData();
  }

  async fetchData() {
    try {
      const response = await fetch('http://localhost:8080/bird-park-basic-info/');
      if (!response.ok) {
        throw new Error('Fehler beim Abrufen der Daten: ' + response.statusText);
      }
      const data = await response.json();
      console.log('Empfangene Daten:', data); // Debugging-Ausgabe
      const { name, address, description, openingHoursResponses, logoUrl } = data;

      const firstOpeningHours = openingHoursResponses[0];
      const { weekday, startTime, endTime } = firstOpeningHours;

      this.setState({
        textFields: {
          Name: name,
          Adresse: address,
          Beschreibung: description,
          LogoUrl: logoUrl,
          Wochentag: weekday,
          Oeffnungszeit: startTime,
          Schließzeit: endTime
        },
        basicData: data // Speichern Sie die abgerufenen Daten in basicData
      });
    } catch (error) {
      console.error('Fehler beim Abrufen der Daten:', error);
    }
  }

  gotoPage = (page) => {
    this.setState({ currentPage: page });
  }

  handleEdit = () => {
    this.setState(prevState => ({
      isEditing: !prevState.isEditing
    }));
  }

  handleSave = async () => {
    const { textFields, basicData } = this.state; // Hinzufügen von basicData zum Zustand
  
    // Konstruktion des Request-Body
    const requestBody = {
      newName: textFields.Name,
      newAddress: textFields.Adresse,
      newDescription: textFields.Beschreibung,
      openingHoursToAdd: [],
      openingHourIdsToRemove: [],
      newLogoUrl: textFields.LogoUrl
    };
  
    // Überprüfen, ob das Feld für den zu löschenden Wochentag nicht leer ist
    if (textFields.WochentagToDelete) {
      // Finden der ID für den zu löschenden Wochentag und Hinzufügen zur Liste der zu löschenden IDs
      const weekdayId = this.findWeekdayId(textFields.WochentagToDelete, basicData); // Übergeben von basicData als Argument
      requestBody.openingHourIdsToRemove.push(weekdayId);
    }
  
    // Überprüfen, ob das Feld für den neuen Wochentag nicht leer ist
    if (textFields.Wochentag) {
      // Hinzufügen der neuen Öffnungszeit, wenn ein Wochentag angegeben ist
      requestBody.openingHoursToAdd.push({
        weekday: textFields.Wochentag,
        startTimeHour: textFields.Oeffnungszeit.split(':')[0],
        startTimeMinute: textFields.Oeffnungszeit.split(':')[1],
        endTimeHour: textFields.Schließzeit.split(':')[0],
        endTimeMinute: textFields.Schließzeit.split(':')[1]
      });
    }
  
    try {
      const response = await fetch('http://localhost:8080/edit-basic-info/', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
      });
      if (response.ok) {
        console.log('Daten erfolgreich gespeichert.');
        this.setState({ isEditing: false });
      } else {
        console.error('Fehler beim Speichern der Daten:', response.statusText);
      }
    } catch (error) {
      console.error('Fehler beim Speichern der Daten:', error);
    }
  }
  

  handleFieldChange = (key, value) => {
    this.setState(prevState => ({
      textFields: {
        ...prevState.textFields,
        [key]: value
      }
    }));
  };

  // Funktion zum Finden der Wochentags-ID
  findWeekdayId = (weekDay, basicData) => {
    console.log('Weekday:', weekDay);
    console.log('Basic Data:', basicData);
    if (!basicData || !basicData.openingHoursResponses || basicData.openingHoursResponses.length === 0) {
      console.error('Ungültige oder leere Daten für das Finden der Wochentags-ID');
      return '0'; // Rückgabe von '0' anstelle von ''
    }
  
    const openingHoursResponses = basicData.openingHoursResponses;
    console.log('Öffnungszeiten:', openingHoursResponses); // Debugging-Ausgabe
    for (let i = 0; i < openingHoursResponses.length; i++) {
      if (openingHoursResponses[i].weekday === weekDay) {
        console.log("ID", openingHoursResponses[i].id.toString());
        return openingHoursResponses[i].id.toString(); // ID als Zeichenfolge zurückgeben
      }
    }
    console.error('Kein Wochentag gefunden:', weekDay); // Debugging-Ausgabe
    return '0'; // Rückgabe von '0' anstelle von ''
  }
  

  render() {
    const { currentPage, textFields, isEditing, basicData } = this.state; // Fügen Sie basicData zum destrukturierten Zustand hinzu
    return (
      <div style={{ position: 'relative' }}>
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

        {currentPage === null && <HomePage
          textFields={textFields}
          isEditing={isEditing}
          onEdit={this.handleEdit}
          onSave={this.handleSave}
          onFieldChange={this.handleFieldChange}
          basicData={basicData} />} {/* Übergeben Sie basicData an HomePage */}
        {currentPage === 'Attraktionen' && <AttractionsPage />}
        {currentPage === 'Touren' && <ToursPage />}
      </div>
    );
  }
}

export default App;