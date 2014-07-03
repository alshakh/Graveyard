/**
     This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mp3organizer.core;

/**
 * This exception is a wrapper for many of JAudtioTagger Exceptions.
 * Happens when jAudioFile throws exception.
 * @author Ahmed Alshakh <ahmed.s.alshakh@gmail.com>
 */
public class ProblemWithAudioFileException extends Exception {
    /**
     * Standard exception constructor.
     * @param msg 
     */
    public ProblemWithAudioFileException(String msg){
        super(msg); 
    }
}