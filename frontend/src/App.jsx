import React from 'react';

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      currentPage: null,
      textFields: {
        Name: '',
        Adresse: '',
        //Zeiten: '',
       // Preise: '',
        Beschreibung: ''
      }
    };
  }

 componentDidMount() {
   // Hier sendest du eine AJAX-Anfrage, um Daten von der REST-API abzurufen
   fetch('http://localhost:8080/bird-park-basic-info/')
     .then(response => response.json())
     .then(data => {
       // Extrahiere die relevanten Daten und aktualisiere den Zustand
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
        {/* Leiste am oberen Bildschirmrand */}
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, backgroundColor: '#24524a', padding: '10px', display: 'flex', justifyContent: 'flex-start', alignItems: 'center' }}>
          <img src="home_button.png" alt="Home" style={{ width: '50px', height: '50px', cursor: 'pointer' }} onClick={() => this.gotoPage(null)} />
          <button style={{ cursor: 'pointer', marginLeft: '50px', color: '#ffffff', backgroundColor: '#ffffff1a', border: 'none', borderRadius: '5px', padding: '5px 10px' }} onClick={() => this.gotoPage('Attraktionen')}>Attraktionen</button>
          <button style={{ cursor: 'pointer', marginLeft: '50px', color: '#ffffff', backgroundColor: '#ffffff1a', border: 'none', borderRadius: '5px', padding: '5px 10px' }} onClick={() => this.gotoPage('Touren')}>Touren</button>
        </div>

        {currentPage === null && (
          <div>
            <img src="logo.png" alt="Home logo" style={{ position: 'absolute', top: '-250px', left: '20px', width: '150px', height: '150px', marginBottom: '200px' }} />
            <div>
              {Object.entries(textFields).map(([key, value], index) => (
                <div
                  key={index}
                  style={{ position: 'absolute', top: `${50 + index * 50}px`, left: '500px', fontSize: '16px', color: 'black' }}
                >
                  <div>{key}: {value}</div>
                </div>
              ))}
            </div>
          </div>
        )}

        {currentPage === 'Attraktionen' && (
          <div>
            <p style={{ position: 'absolute', top: '-300px', left: '20px', fontSize: '24px', color: '#FFFFFFFF' }}>Attraktionen</p>
          </div>
        )}

        {currentPage === 'Touren' && (
          <div>
            <p style={{ position: 'absolute', top: '-300px', left: '20px', fontSize: '24px', color: '#FFFFFFFF' }}>Touren</p>
          </div>
        )}
      </div>
    );
  }
}

export default App;
