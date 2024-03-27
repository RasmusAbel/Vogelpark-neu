import React from 'react';

class HomePage extends React.Component {
  render() {
    const { textFields, isEditing, onEdit, onSave } = this.props;

    return (
      <div style={{ margin: '20px' }}>
        {/* Textfelder anzeigen oder bearbeiten */}
        {isEditing ? (
          <div>
            {Object.entries(textFields).map(([key, value], index) => (
              <div key={index} style={{ marginBottom: '10px' }}>
                <label style={{ marginRight: '10px' }}>{key}:</label>
                <input
                  type="text"
                  value={value}
                  onChange={(e) => this.handleChange(key, e.target.value)}
                />
              </div>
            ))}
            {/* Hinzufügen des Felds zum Löschen eines Wochentags */}
            <div key="WochentagToDelete" style={{ marginBottom: '10px' }}>
              <label style={{ marginRight: '10px' }}>Wochentag zu löschen:</label>
              <input
                type="text"
                value={textFields.WochentagToDelete}
                onChange={(e) => this.handleChange('Wochentag zu löschen:', e.target.value)}
              />
            </div>
            <button onClick={onSave} style={{ marginTop: '10px' }}>Speichern</button>
          </div>
        ) : (
          <div>
            <div style={{ marginBottom: '10px' }}>
              <span style={{ marginRight: '10px' }}>Name:</span>
              <span>{textFields.Name}</span>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <span style={{ marginRight: '10px' }}>Adresse:</span>
              <span>{textFields.Adresse}</span>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <span style={{ marginRight: '10px' }}>Beschreibung:</span>
              <span>{textFields.Beschreibung}</span>
            </div>
            {/* Hinzufügen der neuen Felder */}
            <div style={{ marginBottom: '10px' }}>
              <span style={{ marginRight: '10px' }}>Wochentag:</span>
              <span>{textFields.Wochentag}</span>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <span style={{ marginRight: '10px' }}>Öffnungszeit:</span>
              <span>{textFields.Oeffnungszeit}</span>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <span style={{ marginRight: '10px' }}>Schließzeit:</span>
              <span>{textFields.Schließzeit}</span>
            </div>
            <div style={{ marginBottom: '10px' }}>
              <span style={{ marginRight: '10px' }}>Logo Url:</span>
              <span>{textFields.LogoUrl}</span>
            </div>
            <button onClick={onEdit} style={{ marginTop: '10px' }}>Bearbeiten</button>
          </div>
        )}
      </div>
    );
  }

  handleChange = (key, value) => {
    this.props.onFieldChange(key, value); // Aktualisieren Sie die App-Komponente mit den neuen Werten
  };
}

export default HomePage;
