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
      basicData: null
    };
  }

  //Daten aus Datenbank werden gelesen
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
        basicData: data
      });
    } catch (error) {
      console.error('Fehler beim Abrufen der Daten:', error);
    }
  }

  //Seiten wechsel
  gotoPage = (page) => {
    this.setState({ currentPage: page });
  }

  //Basic Data wird bearbeitet
  handleEdit = () => {
    this.setState(prevState => ({
      isEditing: !prevState.isEditing
    }));
  }

  //Speichern Button wird gedrückt
  handleSave = async () => {
    const { textFields, basicData } = this.state;

    const requestBody = {
      newName: textFields.Name,
      newAddress: textFields.Adresse,
      newDescription: textFields.Beschreibung,
      openingHoursToAdd: [],
      openingHourIdsToRemove: [],
      newLogoUrl: textFields.LogoUrl
    };
  
    if (textFields.WochentagToDelete) {
      const weekdayId = this.findWeekdayId(textFields.WochentagToDelete, basicData); 
      requestBody.openingHourIdsToRemove.push(weekdayId);
    }
  
    if (textFields.Wochentag) {
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
      response.text().then(text => alert(text));
      /*if (response.ok) {
        console.log('Daten erfolgreich gespeichert.');
        this.setState({ isEditing: false });
      } else {
        console.error('Fehler beim Speichern der Daten:', response.statusText);
      }
      */
    } catch (error) {
      console.error('Fehler beim Speichern der Daten:', error);
    }
  }
  
  //DIe Textfelder werdne bearbeitet
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
    if (!basicData || !basicData.openingHoursResponses || basicData.openingHoursResponses.length === 0) {
      console.error('Ungültige oder leere Daten für das Finden der Wochentags-ID');
      return '0'; 
    }
  
    const openingHoursResponses = basicData.openingHoursResponses;
    for (let i = 0; i < openingHoursResponses.length; i++) {
      if (openingHoursResponses[i].weekday === weekDay) {
        return openingHoursResponses[i].id.toString(); 
      }
    }
    console.error('Kein Wochentag gefunden:', weekDay); 
    return '0'; 
  }
  
  render() {
    const { currentPage, textFields, isEditing, basicData } = this.state; 
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