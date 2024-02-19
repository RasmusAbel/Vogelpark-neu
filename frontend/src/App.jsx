import React from 'react';

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      currentPage: null,
      textFields: ["Name", "Adresse", "Zeiten", "Preise", "Beschreibung"]
    };
  }

  gotoPage = (page) => {
    this.setState({ currentPage: page });
  }

  render() {
    const { currentPage, textFields } = this.state;
    return (
      <div style={{ position: 'relative' }}>
        {currentPage === null && (
          <div>
            <img src="logo.png" alt="Logo" style={{position: 'absolute', top: '-200px', left: '500px', width: '150px', height: '150px', marginBottom: '200px'}} />
            <div>
              {textFields.map((text, index) => (
                <div 
                  key={index}
                  style={{ position: 'absolute', top: `${50 + index * 50}px`, left: '500px', fontSize: '16px', color: 'black' }}
                >
                  {text}
                </div>
              ))}
            </div>
            <button style={{ position: 'absolute', top: '300px', left: '300px' }} onClick={() => this.gotoPage('Attraktionen')}>
              <img src="attraktionen_button.png" alt="Attraktionen" style={{ width: '100px', height: '50px' }} />
            </button>
            <button style={{ position: 'absolute', top: '300px', left: '600px' }} onClick={() => this.gotoPage('Touren')}>
              <img src="touren_button.png" alt="Touren" style={{ width: '100px', height: '50px' }} />
            </button>
          </div>
        )}
        {currentPage === 'Attraktionen' && (
          <div>
            <h1>Attraktionen</h1>
            <button style={{ position: 'absolute', top: '200px', left: '200px' }} onClick={() => this.gotoPage(null)}>
              <img src="back_button.png" alt="Zurück zur Hauptseite" style={{ width: '100px', height: '50px' }} />
            </button>
            <button style={{ position: 'absolute', top: '200px', left: '600px' }} onClick={() => this.gotoPage('Touren')}>
              <img src="touren_button.png" alt="Zu Touren" style={{ width: '100px', height: '50px' }} />
            </button>
          </div>
        )}
        {currentPage === 'Touren' && (
          <div>
            <h1>Touren</h1>
            <button style={{ position: 'absolute', top: '200px', left: '200px' }} onClick={() => this.gotoPage(null)}>
              <img src="back_button.png" alt="Zurück zur Hauptseite" style={{ width: '100px', height: '50px' }} />
            </button>
            <button style={{ position: 'absolute', top: '200px', left: '600px' }} onClick={() => this.gotoPage('Attraktionen')}>
              <img src="attraktionen_button.png" alt="Zur Attraktionen" style={{ width: '100px', height: '50px' }} />
            </button>
          </div>
        )}
      </div>
    );
  }
}

export default App;
