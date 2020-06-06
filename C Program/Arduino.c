char Incoming_value = 0;
void setup()
{
Serial.begin(9600);
pinMode(13, OUTPUT);
pinMode(12, OUTPUT);
pinMode(11, OUTPUT);
pinMode(10, OUTPUT);
}
void loop()
{
if(Serial.available() > 0)
{
Incoming_value = Serial.read();
Serial.print(Incoming_value);
Serial.print("\n");

if(Incoming_value == '1')
digitalWrite(13, HIGH);
else if(Incoming_value == '2')
digitalWrite(13, LOW);

else if(Incoming_value == '3')
digitalWrite(12, HIGH);
else if(Incoming_value == '4')
digitalWrite(12, LOW);

else if(Incoming_value == '5')
digitalWrite(11, HIGH);
else if(Incoming_value == '6')
digitalWrite(11, LOW);

else if(Incoming_value == '7')
digitalWrite(10, HIGH);
else if(Incoming_value == '8')
digitalWrite(10, LOW);

}