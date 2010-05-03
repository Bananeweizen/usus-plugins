package org.projectusus.core.internal.proportions.rawdata;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.projectusus.core.internal.proportions.model.Hotspot;
import org.projectusus.core.internal.proportions.model.IHotspot;

public class MiscFileRawData {

    private IFile fileOfRawData;
    private YellowCount yellowCount = new YellowCount();

    public MiscFileRawData( IFile file ) {
        this.fileOfRawData = file;
    }

    public void setYellowCount( int count ) {
        yellowCount.setYellowCount( count );
    }

    public int getViolationCount( CodeProportionKind metric ) {
        return yellowCount.getViolationCount( metric );
    }

    public void addToHotspots( CodeProportionKind metric, List<IHotspot> hotspots ) {
        if( metric == CodeProportionKind.CW && metric.isViolatedBy( this ) ) {
            IHotspot hotspot = yellowCount.createHotspot();
            ((Hotspot)hotspot).setFile( fileOfRawData );
            hotspots.add( hotspot );
        }
    }

    public int getOverallMetric( CodeProportionKind metric ) {
        return yellowCount.getOverallMetric( metric );
    }

}