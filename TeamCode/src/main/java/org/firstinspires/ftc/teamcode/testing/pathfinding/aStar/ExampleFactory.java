/*    
    Copyright (C) 2012 http://software-talk.org/ (developer@software-talk.org)

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

package org.firstinspires.ftc.teamcode.testing.pathfinding.aStar;

/**
 * A simple Factory for example nodes.
 */
public class ExampleFactory implements NodeFactory {

        @Override
        public AbstractNode createNode(int x, int y) {
            return new ExampleNode(x, y);
        }

}
